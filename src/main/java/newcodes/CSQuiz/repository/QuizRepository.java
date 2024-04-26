package newcodes.CSQuiz.repository;

import java.util.List;
import java.util.Optional;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerDTO;

public interface QuizRepository {
    Quiz save(Quiz quiz, List<AnswerDTO> answerRequest);
    List<Quiz> findAll();
    Optional<Quiz> findById(int id);
    void delete(int id);
}
