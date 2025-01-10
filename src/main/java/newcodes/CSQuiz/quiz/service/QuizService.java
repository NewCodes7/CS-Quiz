package newcodes.CSQuiz.quiz.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.exception.QuizNotFoundException;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.quiz.repository.JdbcTemplatePendingQuizRepository;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final JdbcTemplatePendingQuizRepository quizPendingRepository;

    public List<QuizViewDto> findQuizzes(int userId, String kw, List<String> categories, List<String> statuses) {
        return quizRepository.findQuizzes(userId, kw, categories, statuses);
    }

    public Quiz findQuizById(int id) {
        return quizRepository.findQuizById(id)
                .orElseThrow(() -> new QuizNotFoundException(id));
    }

    public Map<Answer, List<AlternativeAnswer>> findAnswersById(int id) {
        return quizRepository.findAnswersById(id);
    }

    public void delete(int id) {
        quizRepository.deleteQuizById(id);
    }

    public PendingQuiz createQuizRequest(String requestBody) {
        PendingQuiz quizRequest = PendingQuiz.builder()
                .requestBody(requestBody)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        quizPendingRepository.savePendingQuiz(quizRequest);
        return quizRequest;
    }
}
