package newcodes.CSQuiz.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import newcodes.CSQuiz.domain.Submission;
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
    public Submission save(Submission submission) {
        String sql = "INSERT INTO submissions(user_id, quiz_id, correct, submission_date) VALUES(?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, submission.getUserId());
            pstmt.setInt(2, submission.getQuizId());
            pstmt.setBoolean(3, submission.getCorrect());
            return pstmt;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            submission.setSubmissionId(keyHolder.getKey().intValue());
        }
//        else {
//            throw new SQLException("ID 조회 실패");
//        }

        return submission;
    }
}
