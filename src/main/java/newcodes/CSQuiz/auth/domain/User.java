package newcodes.CSQuiz.auth.domain;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer user_id;
    private String email;
    private String password_hashed;
    private String registration_date;
    private String username;
    private Set<Role> roles;
}
