package newcodes.CSQuiz.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuizUserRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    public QuizUserRequestRepository(DataSource dataSource) { // 데이터 소스의 역할? 생성자 하나면 Autowired 생략 가능
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int save(QuizUserRequest quizRequest) {
        String sql = "INSERT INTO quiz_requests (request_body, status, created_at, updated_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, quizRequest.getRequestBody(), quizRequest.getStatus(),
                quizRequest.getCreatedAt(), quizRequest.getUpdatedAt());
    }

    public List<QuizUserRequest> findByStatus(String status) {
        String sql = "SELECT * FROM quiz_requests WHERE status = ?";
        return jdbcTemplate.query(sql, new Object[]{status}, new BeanPropertyRowMapper<>(QuizUserRequest.class));
    }

    public int updateStatus(Long id, String status) {
        String sql = "UPDATE quiz_requests SET status = ?, updated_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, LocalDateTime.now(), id);
    }

    public QuizUserRequest findById(Long id) {
        String sql = "SELECT * FROM quiz_requests WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(QuizUserRequest.class));
    }
}
