package newcodes.CSQuiz.quiz.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Answer {
    private Integer answerId;
    private Integer quizId;
    private String answerText;
}
