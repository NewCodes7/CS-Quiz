package newcodes.CSQuiz.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UpdateQuizRequest {
    private QuizDTO quiz;
    private List<AnswerDTO> answers;
}
