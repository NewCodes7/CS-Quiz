package newcodes.CSQuiz.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import newcodes.CSQuiz.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class JdbcTemplateUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            User user = User.builder()
                    .password_hashed(rs.getString("password_hashed"))
                    .email(rs.getString("email"))
                    .username(rs.getString("username"))
                    .build();
            return user;
        }).stream().findFirst();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users(email, password_hashed, username, registration_date) VALUES(?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword_hashed());
            pstmt.setString(3, user.getEmail()); // 임시로 username을 email로 처리
            return pstmt;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setUser_id(keyHolder.getKey().intValue());
        }
//        else {
//            throw new SQLException("ID 조회 실패");
//        }

        return user;
    }
}

