package newcodes.CSQuiz.quiz.dto;

import java.util.List;
import lombok.Getter;
import newcodes.CSQuiz.answer.dto.AnswerDTO;

@Getter
public class QuizUpdateRequest {
    private QuizDTO quiz;
    private List<AnswerDTO> answers;
}
