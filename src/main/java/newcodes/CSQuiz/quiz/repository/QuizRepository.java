package newcodes.CSQuiz.quiz.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;

public interface QuizRepository {
    Quiz save(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers);
    List<QuizViewDTO> findQuizzes(int userId, String kw, List<String> categories, List<String> statuses);
    List<Quiz> findAll();
    Optional<Quiz> findById(int id);
    Map<Answer, List<AlternativeAnswer>> findAnswersById(int id);
    void delete(int id);
    Quiz update(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers);
}
