package newcodes.CSQuiz.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AnswerResponse {
    private int userId;
    private int quizId;
    private Boolean isAllCorrect;
    private List<Boolean> isCorrect;
}
