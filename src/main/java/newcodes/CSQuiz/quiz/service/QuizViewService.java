package newcodes.CSQuiz.quiz.service;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.service.SubmissionService;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizViewService {

    private final SubmissionService submissionService;
    private final QuizService quizService;

    public QuizViewDto createQuizView(Quiz quiz, Integer userId) {
        // 정답 개수 설정
        quiz.setAnswerCounts(quizService.findAnswersById(quiz.getQuizId()).size());

        // 사용자의 정답 여부 확인
        boolean isCorrect = false;
        if (userId != null) {
            isCorrect = submissionService.findById(userId, quiz.getQuizId());
        }

        QuizViewDto quizViewDto = new QuizViewDto(quiz);
        quizViewDto.setIsCorrect(isCorrect);

        return quizViewDto;
    }
}
