package newcodes.CSQuiz.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BlacklistedToken {

    private Long id;
    private String token;

    public BlacklistedToken(String token) {
        this.token = token;
    }
}
