package newcodes.CSQuiz.quiz.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import newcodes.CSQuiz.answer.dto.AnswerDTO;

@Getter
@Setter
public class QuizRequest {
    private QuizDTO quiz;
    private List<AnswerDTO> answers;
}
