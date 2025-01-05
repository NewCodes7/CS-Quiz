package newcodes.CSQuiz.quiz.repository;

import java.util.List;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;

public interface PendingQuizRepository {
    long savePendingQuiz(PendingQuiz quizRequest);

    List<PendingQuiz> findPendingQuizByStatus(String status);

    long updatePendingQuizStatus(Long id, String status);

    PendingQuiz findPendingQuizById(Long id);
}