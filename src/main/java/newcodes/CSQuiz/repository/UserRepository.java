package newcodes.CSQuiz.repository;

import java.util.Optional;
import newcodes.CSQuiz.domain.User;
import org.springframework.stereotype.Repository;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}