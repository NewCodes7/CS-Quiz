package newcodes.CSQuiz.user.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.config.jwt.RefreshTokenValidator;
import newcodes.CSQuiz.user.config.jwt.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ManagerReissueTokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenValidator refreshTokenValidator;

    public String reissueToken(final String refreshToken) {
        refreshTokenValidator.validateToken(refreshToken);
        refreshTokenValidator.validateLogoutToken(refreshToken);
        final Integer memberId = tokenProvider.getUserId(refreshToken);

        return tokenProvider.generateToken(memberId, Duration.ofDays(7));
    }
}
