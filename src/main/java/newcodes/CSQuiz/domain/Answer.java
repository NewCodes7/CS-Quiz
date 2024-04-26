package newcodes.CSQuiz.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Answer {
    private int answerId;
    private int quizId;
    private String answerText;
}
