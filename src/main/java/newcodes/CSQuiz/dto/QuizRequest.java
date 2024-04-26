package newcodes.CSQuiz.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizRequest {
    private QuizDTO quiz;
    private List<AnswerDTO> answers;
}
