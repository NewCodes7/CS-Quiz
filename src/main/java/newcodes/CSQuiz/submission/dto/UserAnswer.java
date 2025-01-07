package newcodes.CSQuiz.submission.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {
    @NotBlank(message = "공백을 입력할 수 없어요!")
    private String userAnswer;
}
