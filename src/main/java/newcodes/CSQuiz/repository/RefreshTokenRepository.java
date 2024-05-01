package newcodes.CSQuiz.repository;

import java.util.Optional;
import newcodes.CSQuiz.domain.RefreshToken;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUserId(Integer userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken save(RefreshToken refreshToken);
}
