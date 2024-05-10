package newcodes.CSQuiz.answer.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

// 유저가 보내는 정답 submit
@Getter
@Setter
public class AnswerRequest {
    private int quizId;
    private int userId;
    private String[] userAnswers;
}
