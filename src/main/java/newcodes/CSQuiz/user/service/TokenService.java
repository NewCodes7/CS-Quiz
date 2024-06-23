package newcodes.CSQuiz.user.service;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.config.jwt.TokenProvider;
import newcodes.CSQuiz.user.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Integer userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();

        return tokenProvider.generateToken(userId, Duration.ofMinutes(30));
    }
}
