package newcodes.CSQuiz.user.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.config.jwt.TokenProvider;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Integer userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();

        return tokenProvider.generateToken(userId, Duration.ofMinutes(30));
    }
}
