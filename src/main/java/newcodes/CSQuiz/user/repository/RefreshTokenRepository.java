package newcodes.CSQuiz.user.repository;

import java.util.Optional;
import newcodes.CSQuiz.user.domain.RefreshToken;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUserId(Integer userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    RefreshToken save(RefreshToken refreshToken);
}
