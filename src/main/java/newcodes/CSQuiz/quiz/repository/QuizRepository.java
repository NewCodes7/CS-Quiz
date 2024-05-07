package newcodes.CSQuiz.quiz.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import newcodes.CSQuiz.answer.domain.AlternativeAnswer;
import newcodes.CSQuiz.answer.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;

public interface QuizRepository {
    Quiz save(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers);
    List<QuizViewDTO> findQuizzes(int pageNumber, int pageSize, String kw, List<String> categories);
    List<Quiz> findAll();
    Optional<Quiz> findById(int id);
    Map<Answer, List<AlternativeAnswer>> findAnswersById(int id);
    void delete(int id);
    Quiz update(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers);
}
