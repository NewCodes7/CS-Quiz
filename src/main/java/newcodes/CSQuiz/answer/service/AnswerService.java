package newcodes.CSQuiz.answer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.answer.dto.AnswerRequest;
import newcodes.CSQuiz.answer.dto.AnswerResponse;
import newcodes.CSQuiz.answer.domain.Submission;
import newcodes.CSQuiz.answer.dto.SubmissionDTO;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import newcodes.CSQuiz.answer.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuizRepository quizRepository; // REFACTOR: quiz 패키지 의존 문제
    private final SubmissionRepository submissionRepository;

    @Transactional
    public AnswerResponse check(AnswerRequest request) {
        if (request.getUserAnswers() == null || request.getUserAnswers().length == 0) {
            throw new IllegalArgumentException("사용자가 답을 입력하지 않았습니다.");
        }

        Map<Answer, List<AlternativeAnswer>> answers = quizRepository.findAnswersById(request.getQuizId());

        String[] userAnswers = request.getUserAnswers();
        Boolean[] isCorrects = new Boolean[userAnswers.length];
        boolean isAllCorrect = true;

        for (int i = 0; i < userAnswers.length; i++) {
            String userAnswer = userAnswers[i];
            boolean isCurrentCorrect = false;

            for (Answer answer : answers.keySet()) {
                if (userAnswer.equals(answer.getAnswerText())) {
                    isCurrentCorrect = true;
                    break;
                }

                for (AlternativeAnswer alternativeAnswer : answers.get(answer)) {
                    if (userAnswer.equals(alternativeAnswer.getAlternativeText())) {
                        isCurrentCorrect = true;
                        break;
                    }
                }
            }

            isCorrects[i] = isCurrentCorrect;
            if (!isCurrentCorrect) {
                isAllCorrect = false;
            }
        }

        return AnswerResponse.builder()
                .userId(request.getUserId())
                .quizId(request.getQuizId())
                .isCorrect(Arrays.asList(isCorrects))
                .isAllCorrect(isAllCorrect)
                .build();
    }

    public Submission save(SubmissionDTO submissionDTO) {
        return submissionRepository.save(submissionDTO.toEntity());
    }
}
