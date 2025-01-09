package newcodes.CSQuiz.auth.repository;

import java.util.Optional;
import newcodes.CSQuiz.auth.domain.User;

public interface AuthRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer id);

    User save(User user);
}