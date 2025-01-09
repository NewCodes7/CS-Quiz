package newcodes.CSQuiz.auth.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import newcodes.CSQuiz.auth.domain.Role;
import newcodes.CSQuiz.auth.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateAuthRepository implements AuthRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        Optional<User> user = jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            User user1 = User.builder()
                    .password_hashed(rs.getString("password_hashed"))
                    .email(rs.getString("email"))
                    .username(rs.getString("username"))
                    .user_id(rs.getInt("user_id"))
                    .build();
            return user1;
        }).stream().findFirst();

        if (user.isPresent()) {
            String roleSql = "SELECT r.name FROM user_role ur JOIN role r ON ur.role_id = r.id WHERE ur.user_id = ?";
            Set<Role> roles = new HashSet<>(
                    jdbcTemplate.query(roleSql, new Object[]{user.get().getUser_id()}, (rs, rowNum) -> {
                        Role role = Role.builder()
                                .name(rs.getString("name"))
                                .build();
                        return role;
                    }));
            user.get().setRoles(roles);
        }

        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
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

        return user;
    }
}

