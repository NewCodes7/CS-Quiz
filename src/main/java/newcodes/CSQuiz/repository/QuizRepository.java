package newcodes.CSQuiz.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import newcodes.CSQuiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.domain.Answer;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerDTO;

public interface QuizRepository {
    Quiz save(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers);
    List<Quiz> findAll();
    Optional<Quiz> findById(int id);
    Map<Answer, List<AlternativeAnswer>> findAnswersById(int id);
    void delete(int id);
}
