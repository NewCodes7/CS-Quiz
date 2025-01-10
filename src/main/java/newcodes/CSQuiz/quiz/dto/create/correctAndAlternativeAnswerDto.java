package newcodes.CSQuiz.quiz.dto.create;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;

@Getter
@Setter
@Builder
public class correctAndAlternativeAnswerDto {
    private String correctAnswer;
    private List<String> alternativeAnswers;

    public Answer toAnswerEntity() {
        return Answer.builder()
                .answerText(correctAnswer)
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