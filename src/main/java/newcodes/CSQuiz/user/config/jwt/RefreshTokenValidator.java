package newcodes.CSQuiz.user.config.jwt;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.exception.UnAuthorizationException;
import newcodes.CSQuiz.user.repository.BlacklistedTokenRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTokenValidator {

    private final TokenProvider tokenProvider;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public void validateToken(String refreshToken) {
        if (!tokenProvider.isValidToken(refreshToken)) {
            throw new UnAuthorizationException("[ERROR] 유효하지 않은 Refresh Token입니다!");
        }
    }

    public void validateTokenOwnerId(String refreshToken, Integer id) {
        final Integer ownerId = tokenProvider.getUserId(refreshToken);
        if (!ownerId.equals(id)) {
            throw new UnAuthorizationException("[ERROR] 로그인한 사용자의 Refresh Token이 아닙니다!");
        }
    }

    public void validateLogoutToken(String refreshToken) {
        if (blacklistedTokenRepository.findByToken(refreshToken).isPresent()) {
            throw new UnAuthorizationException("[ERROR] 이미 로그아웃된 사용자입니다!");
        }
    }
}
