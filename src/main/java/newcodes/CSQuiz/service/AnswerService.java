package newcodes.CSQuiz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.domain.Answer;
import newcodes.CSQuiz.dto.AnswerRequest;
import newcodes.CSQuiz.dto.AnswerResponse;
import newcodes.CSQuiz.domain.Submission;
import newcodes.CSQuiz.dto.SubmissionDTO;
import newcodes.CSQuiz.repository.QuizRepository;
import newcodes.CSQuiz.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuizRepository quizRepository;
    private final SubmissionRepository submissionRepository;

    public AnswerResponse check(AnswerRequest request) {
        // FIXME: request에서 user가 답을 입력하지 않은 경우 예외처리

        Map<Answer, List<AlternativeAnswer>> answers = quizRepository.findAnswersById(request.getQuizId());

        AnswerResponse answerResponse = AnswerResponse.builder()
                .userId(request.getUserId())
                .quizId(request.getQuizId())
                .build();

        int index = 0;
        boolean isAllCorrect = true;
        String[] userAnswers = request.getUserAnswers();
        List<Boolean> isCorrects = new ArrayList<>();


        for (Answer answer : answers.keySet()) {
            if (userAnswers[index].equals(answer.getAnswerText())) { // 불리언 따로?
                isCorrects.add(true);
            } else {
                boolean isCorrect = false;

                // 대체 답 확인
                for (AlternativeAnswer alternativeAnswer : answers.get(answer)) {
                    if (userAnswers[index].equals(alternativeAnswer.getAlternativeText())) {
                        isCorrect = true;
                        break;
                    }
                }

                isCorrects.add(isCorrect);
            }

            if (isCorrects.get(index) == false) {
                isAllCorrect = false;
            }

            index++;
        }

        answerResponse.setIsCorrect(isCorrects);
        answerResponse.setIsAllCorrect(isAllCorrect);

        return answerResponse;
    }

    public Submission save(SubmissionDTO submissionDTO) {
        return submissionRepository.save(submissionDTO.toEntity());
    }
}
