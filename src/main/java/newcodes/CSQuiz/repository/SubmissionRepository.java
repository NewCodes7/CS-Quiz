package newcodes.CSQuiz.repository;

import java.util.List;
import newcodes.CSQuiz.domain.Submission;

public interface SubmissionRepository {
    Submission save(Submission submission);
    Boolean findById(int userId, int quizId);
//    List<Boolean>
}
