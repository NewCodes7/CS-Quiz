package newcodes.CSQuiz.user.domain;

import java.util.HashSet;
import java.util.Set;
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

    private Set<Role> roles;
}
