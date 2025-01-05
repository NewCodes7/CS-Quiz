package newcodes.CSQuiz.quiz.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.quiz.dto.create.QuizCreateDto;

public interface QuizRepository {
    Quiz saveQuizAndAnswers(QuizCreateDto quizCreateDto);

    List<QuizViewDto> findQuizzes(int userId, String kw, List<String> categories, List<String> statuses);

    Optional<Quiz> findQuizById(int id);

    Map<Answer, List<AlternativeAnswer>> findAnswersById(int id);

    void deleteQuizById(int id);
}
