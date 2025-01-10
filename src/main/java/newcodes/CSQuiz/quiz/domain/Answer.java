package newcodes.CSQuiz.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class Answer {
    private Integer answerId;
    private Integer quizId;
    private String answerText;
}
