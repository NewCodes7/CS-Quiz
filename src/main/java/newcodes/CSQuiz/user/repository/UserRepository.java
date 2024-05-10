package newcodes.CSQuiz.user.repository;

import java.util.Optional;
import newcodes.CSQuiz.user.domain.User;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
    User save(User user);
}