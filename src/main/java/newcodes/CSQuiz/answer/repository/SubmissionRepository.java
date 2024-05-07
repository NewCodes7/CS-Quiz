package newcodes.CSQuiz.answer.repository;

import newcodes.CSQuiz.answer.domain.Submission;

public interface SubmissionRepository {
    Submission save(Submission submission);
    Boolean findById(int userId, int quizId);
//    List<Boolean>
}
