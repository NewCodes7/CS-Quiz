package newcodes.CSQuiz.user.domain;

import lombok.Getter;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    private Integer id;

    private Integer userId;

    private String refreshToken;

    public RefreshToken(Integer userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;

        return this;
    }
}
