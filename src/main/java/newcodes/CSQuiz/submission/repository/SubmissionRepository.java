package newcodes.CSQuiz.submission.repository;

import newcodes.CSQuiz.submission.dto.SubmissionResponse;

public interface SubmissionRepository {
    SubmissionResponse save(SubmissionResponse submissionResponse);

    Boolean findById(int userId, int quizId);
}
