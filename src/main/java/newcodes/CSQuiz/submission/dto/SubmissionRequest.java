package newcodes.CSQuiz.submission.dto;

import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
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
public class SubmissionRequest {
    private int quizId;
    private int userId;

    @Size(min = 1, message = "사용자가 답을 입력하지 않았습니다.")
    private List<UserAnswer> userAnswers = new ArrayList<>();
}
