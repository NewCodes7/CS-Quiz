package newcodes.CSQuiz.quiz.service;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.service.SubmissionService;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizViewService {

    private final SubmissionService submissionService;
    private final QuizApiService quizApiService;

    public QuizViewDTO createQuizViewDTO(Quiz quiz, Integer userId) {
        // 정답 개수 설정
        quiz.setAnswerCounts(quizApiService.findAnswersById(quiz.getQuizId()).size());

        // 사용자의 정답 여부 확인
        boolean isCorrect = false;
        if (userId != null) {
            isCorrect = submissionService.findById(userId, quiz.getQuizId());
        }

        QuizViewDTO quizViewDTO = new QuizViewDTO(quiz);
        quizViewDTO.setIsCorrect(isCorrect);

        return quizViewDTO;
    }
}
