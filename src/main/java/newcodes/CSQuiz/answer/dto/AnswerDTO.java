package newcodes.CSQuiz.answer.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;

@Getter
@Setter
public class AnswerDTO {
    private String answerText;
    private List<String> alternativeAnswers;

    public Answer toAnswerEntity() {
        return Answer.builder()
                .answerText(answerText)
                .build();
    }

    public List<AlternativeAnswer> toAlternativeAnswerEntity() {
        List<AlternativeAnswer> alternativeAnswerList = new ArrayList<>();

        for (String alternativeAnswer : alternativeAnswers) {
            alternativeAnswerList.add(AlternativeAnswer.builder()
                    .alternativeText(alternativeAnswer)
                    .build());
        }

        return alternativeAnswerList;
    }
}