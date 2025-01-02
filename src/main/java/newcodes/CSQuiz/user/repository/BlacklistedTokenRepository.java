package newcodes.CSQuiz.user.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import newcodes.CSQuiz.user.domain.BlacklistedToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BlacklistedTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public BlacklistedTokenRepository(DataSource dataSource) { // 데이터 소스의 역할? 생성자 하나면 Autowired 생략 가능
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<BlacklistedToken> findByToken(String token) {
        String sql = "SELECT * FROM blacklisted_token where token = ?";
        List<BlacklistedToken> result = jdbcTemplate.query(sql, rowMapper(), token);

        return result.stream().findAny();
    }

    public BlacklistedToken save(BlacklistedToken token) {
        String sql = "INSERT INTO blacklisted_token(token) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, token.getToken());
            return pstmt;
        }, keyHolder);

        return token;
    }

    private RowMapper<BlacklistedToken> rowMapper() {
        return (rs, rowNum) -> {
            BlacklistedToken blacklistedToken = new BlacklistedToken(
                    rs.getLong("id"),
                    rs.getString("token"));
            return blacklistedToken;
        };
    }
}
