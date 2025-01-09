package newcodes.CSQuiz.submission.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import newcodes.CSQuiz.submission.domain.Submission;
import newcodes.CSQuiz.submission.dto.SubmissionResponse;
import newcodes.CSQuiz.submission.dto.UserAnswer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateSubmissionRepository implements SubmissionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateSubmissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SubmissionResponse saveUserSubmission(SubmissionResponse submissionResponse) {
        String sql = "INSERT INTO submissions(user_id, quiz_id, correct, submission_date) VALUES(?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, submissionResponse.getUserId());
            pstmt.setInt(2, submissionResponse.getQuizId());
            pstmt.setBoolean(3, submissionResponse.getIsAllCorrect());
            return pstmt;
        }, keyHolder);

        int submissionId = keyHolder.getKey().intValue();
        submissionResponse.setSubmissionId(submissionId);

        // 유저가 제출한 답안 리스트 저장 REFACTOR: I/O 횟수 줄이기
        String answerSql = "INSERT INTO submission_answers(submission_id, answer_text) VALUES(?, ?)";
        List<UserAnswer> userAnswers = submissionResponse.getUserAnswers();

        for (UserAnswer answer : userAnswers) {
            jdbcTemplate.update(answerSql, submissionId, answer.getUserAnswer());
        }

        return submissionResponse;
    }

    @Override
    public Boolean findById(int userId, int quizId) {
        String sql = "SELECT * FROM submissions WHERE user_id = ? AND quiz_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId, quizId}, (rs, rowNum) -> {
            Submission submission = Submission.builder()
                    .correct(rs.getBoolean("correct"))
                    .submissionDate(rs.getString("submission_date"))
                    .userId(rs.getInt("user_id"))
                    .quizId(rs.getInt("quiz_id"))
                    .build();
            return submission;
        }).stream().anyMatch(Submission::getCorrect);
    }
}
