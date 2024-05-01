package newcodes.CSQuiz.repository;

import newcodes.CSQuiz.domain.Submission;

public interface SubmissionRepository {
    Submission save(Submission submission);
}
