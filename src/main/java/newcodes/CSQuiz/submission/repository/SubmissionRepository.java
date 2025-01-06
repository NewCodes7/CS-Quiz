package newcodes.CSQuiz.submission.repository;

import newcodes.CSQuiz.submission.domain.Submission;

public interface SubmissionRepository {
    Submission save(Submission submission);

    Boolean findById(int userId, int quizId);
}
