package newcodes.CSQuiz.repository;

import java.util.Optional;
import newcodes.CSQuiz.domain.User;
import org.springframework.stereotype.Repository;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
    User save(User user);
}