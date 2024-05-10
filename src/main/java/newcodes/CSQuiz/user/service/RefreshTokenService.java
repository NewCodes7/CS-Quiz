package newcodes.CSQuiz.user.service;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.domain.RefreshToken;
import newcodes.CSQuiz.user.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
