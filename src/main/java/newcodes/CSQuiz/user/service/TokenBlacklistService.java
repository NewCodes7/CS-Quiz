package newcodes.CSQuiz.user.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.config.jwt.RefreshTokenValidator;
import newcodes.CSQuiz.user.domain.BlacklistedToken;
import newcodes.CSQuiz.user.repository.BlacklistedTokenRepository;
import newcodes.CSQuiz.user.repository.RefreshTokenRepository;
import newcodes.CSQuiz.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final RefreshTokenValidator refreshTokenValidator;

    public void logout(Integer id, String refreshToken) {
        refreshTokenValidator.validateToken(refreshToken);
        refreshTokenValidator.validateTokenOwnerId(refreshToken, id);
        refreshTokenValidator.validateLogoutToken(refreshToken);

        BlacklistedToken blacklistedToken = new BlacklistedToken(refreshToken);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }
}
