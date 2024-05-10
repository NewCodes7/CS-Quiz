package newcodes.CSQuiz.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class User {
    private Integer user_id;
    private String email;
    private String password_hashed;
    private String registration_date;
    private String username;
}
