package newcodes.CSQuiz.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {
    private String answerText;
    private List<String> alternativeAnswers;
}