package newcodes.CSQuiz.repository;

import java.util.List;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerDTO;

public interface QuizRepository {
    Quiz save(Quiz quiz, List<AnswerDTO> answerRequest);
    List<Quiz> findAll();
}
