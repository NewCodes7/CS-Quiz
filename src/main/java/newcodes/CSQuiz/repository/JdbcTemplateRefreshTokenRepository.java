package newcodes.CSQuiz.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.domain.RefreshToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateRefreshTokenRepository implements RefreshTokenRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateRefreshTokenRepository(DataSource dataSource) { // 데이터 소스의 역할? 생성자 하나면 Autowired 생략 가능
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Integer userId) {
        String sql = "SELECT * FROM RefreshToken where user_id = ?";
        List<RefreshToken> result = jdbcTemplate.query(sql, rowMapper(), userId);

        return result.stream().findAny();
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        String sql = "SELECT * FROM RefreshToken where refresh_token = ?";
        List<RefreshToken> result = jdbcTemplate.query(sql, rowMapper(), refreshToken);

        return result.stream().findAny();
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        String sql = "INSERT INTO RefreshToken(user_id, refresh_token) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, refreshToken.getUserId());
            pstmt.setString(2, refreshToken.getRefreshToken());
            return pstmt;
        }, keyHolder);

//        if (keyHolder.getKey() != null) {
//            refreshToken.(keyHolder.getKey().intValue());
//        }
//        else {
//            throw new SQLException("ID 조회 실패");
//        }

        return refreshToken;
    }

    private RowMapper<RefreshToken> rowMapper() {
        return (rs, rowNum) -> {
            RefreshToken refreshToken = new RefreshToken(
                    rs.getInt("user_id"),
                    rs.getString("refresh_token"));
            return refreshToken;
        };
    }
}
