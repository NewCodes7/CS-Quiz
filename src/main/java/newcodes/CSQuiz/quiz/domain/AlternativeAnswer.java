package newcodes.CSQuiz.quiz.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AlternativeAnswer {
    private Integer alternativeId;
    private Integer answerId;
    private String alternativeText;
}
