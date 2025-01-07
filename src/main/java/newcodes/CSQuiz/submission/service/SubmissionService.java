package newcodes.CSQuiz.submission.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.common.util.StringUtils;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import newcodes.CSQuiz.submission.domain.Submission;
import newcodes.CSQuiz.submission.dto.SubmissionDto;
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

    @Transactional
    public SubmissionResponse gradeSubmission(SubmissionRequest request) {
        Map<Answer, List<AlternativeAnswer>> answers = quizRepository.findAnswersById(request.getQuizId());

        List<UserAnswer> userAnswers = request.getUserAnswers();

        Boolean[] isCorrects = userAnswers.stream()
                .map(userAnswer -> {
                    String normalizedUserAnswer = StringUtils.normalize(userAnswer.getUserAnswer());

                    return answers.entrySet().stream()
                            .anyMatch(entry -> {
                                Answer answer = entry.getKey();
                                String normalizedAnswer = StringUtils.normalize(answer.getAnswerText());

                                if (normalizedUserAnswer.equals(normalizedAnswer)) {
                                    return true;
                                }

                                return entry.getValue().stream()
                                        .map(alt -> StringUtils.normalize(alt.getAlternativeText()))
                                        .anyMatch(normalizedUserAnswer::equals);
                            });
                })
                .toArray(Boolean[]::new);
        boolean isAllCorrect = Arrays.stream(isCorrects).allMatch(Boolean::booleanValue);

        return SubmissionResponse.builder()
                .userId(request.getUserId())
                .quizId(request.getQuizId())
                .isCorrect(Arrays.asList(isCorrects))
                .isAllCorrect(isAllCorrect)
                .build();
    }

    public Submission save(SubmissionDto submissionDTO) {
        return submissionRepository.save(submissionDTO.toEntity());
    }

    public Boolean findById(int userId, int quizId) {
        return submissionRepository.findById(userId, quizId);
    }
}
