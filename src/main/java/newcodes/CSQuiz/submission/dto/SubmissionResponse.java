package newcodes.CSQuiz.submission.dto;

import java.util.List;
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
public class SubmissionResponse {
    private int submissionId;
    private int userId;
    private int quizId;
    private List<UserAnswer> userAnswers;
    private Boolean isAllCorrect;
    private List<Boolean> isCorrect;
}
