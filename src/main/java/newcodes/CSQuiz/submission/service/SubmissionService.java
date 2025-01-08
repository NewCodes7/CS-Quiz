package newcodes.CSQuiz.submission.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.common.util.StringUtils;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import newcodes.CSQuiz.submission.dto.SubmissionRequest;
import newcodes.CSQuiz.submission.dto.SubmissionResponse;
import newcodes.CSQuiz.submission.dto.UserAnswer;
import newcodes.CSQuiz.submission.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final QuizRepository quizRepository; // REFACTOR: quiz 패키지 의존 문제
    private final SubmissionRepository submissionRepository;

    public Boolean findById(int userId, int quizId) {
        return submissionRepository.findById(userId, quizId);
    }

    @Transactional
    public SubmissionResponse gradeSubmission(SubmissionRequest request) {
        Map<Answer, List<AlternativeAnswer>> answers = quizRepository.findAnswersById(request.getQuizId());
        List<Boolean> gradingResults = gradeUserAnswers(request.getUserAnswers(), answers);
        boolean isAllCorrect = gradingResults.stream().allMatch(Boolean::booleanValue);

        SubmissionResponse submissionResponse = SubmissionResponse.builder()
                .userId(request.getUserId())
                .quizId(request.getQuizId())
                .isCorrect(gradingResults)
                .isAllCorrect(isAllCorrect)
                .build();
        ;
        return save(submissionResponse);
    }

    private List<Boolean> gradeUserAnswers(List<UserAnswer> userAnswers,
                                           Map<Answer, List<AlternativeAnswer>> answers) {
        return userAnswers.stream()
                .map(userAnswer -> isCorrectAnswer(userAnswer, answers))
                .collect(Collectors.toList());
    }

    private boolean isCorrectAnswer(UserAnswer userAnswer,
                                    Map<Answer, List<AlternativeAnswer>> answers) {
        String normalizedUserAnswer = StringUtils.normalize(userAnswer.getUserAnswer());

        return answers.entrySet().stream()
                .anyMatch(entry -> {
                    if (isMainAnswerMatch(normalizedUserAnswer, entry.getKey())) {
                        return true;
                    }
                    return isAlternativeAnswerMatch(normalizedUserAnswer, entry.getValue());
                });
    }

    private boolean isMainAnswerMatch(String normalizedUserAnswer, Answer answer) {
        return normalizedUserAnswer.equals(StringUtils.normalize(answer.getAnswerText()));
    }

    private boolean isAlternativeAnswerMatch(String normalizedUserAnswer,
                                             List<AlternativeAnswer> alternatives) {
        return alternatives.stream()
                .map(alt -> StringUtils.normalize(alt.getAlternativeText()))
                .anyMatch(normalizedUserAnswer::equals);
    }

    private SubmissionResponse save(SubmissionResponse submissionResponse) {
        return submissionRepository.save(submissionResponse);
    }
}
