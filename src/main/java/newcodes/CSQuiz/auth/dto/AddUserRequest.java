package newcodes.CSQuiz.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddUserRequest {
    private String email;
    private String password_hashed;
    private String username;
}
