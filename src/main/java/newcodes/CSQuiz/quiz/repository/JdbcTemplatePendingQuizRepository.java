package newcodes.CSQuiz.quiz.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import newcodes.CSQuiz.common.JsonFormatter;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplatePendingQuizRepository implements PendingQuizRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePendingQuizRepository(DataSource dataSource) { // 데이터 소스의 역할? 생성자 하나면 Autowired 생략 가능
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long savePendingQuiz(PendingQuiz pendingQuiz) {
        String sql = "INSERT INTO quizzes_pending (request_body, status, created_at, updated_at) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // id 컬럼 반환 요청
            ps.setString(1, pendingQuiz.getRequestBody());
            ps.setString(2, pendingQuiz.getStatus());
            ps.setTimestamp(3, Timestamp.valueOf(pendingQuiz.getCreatedAt()));
            ps.setTimestamp(4, Timestamp.valueOf(pendingQuiz.getUpdatedAt()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public PendingQuiz findPendingQuizById(Long id) {
        String sql = "SELECT * FROM quizzes_pending WHERE id = ?";
        try {
            PendingQuiz pendingQuiz = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(PendingQuiz.class),
                    id
            );
            if (pendingQuiz != null) {
                pendingQuiz.setRequestBody(JsonFormatter.formatJson(pendingQuiz.getRequestBody()));
            }
            return pendingQuiz;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Quiz not found with id: " + id, 1);
        }
    }

    public List<PendingQuiz> findPendingQuizByStatus(String status) {
        String sql = "SELECT * FROM quizzes_pending WHERE status = ?";
        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(PendingQuiz.class),
                status
        );
    }

    public long updatePendingQuizStatus(Long id, String status) {
        String sql = "UPDATE quizzes_pending SET status = ?, updated_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, LocalDateTime.now(), id);
    }
}
