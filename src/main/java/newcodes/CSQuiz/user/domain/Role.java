package newcodes.CSQuiz.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Role {
    private Integer id;
    private String name;
}
