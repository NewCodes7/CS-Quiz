package newcodes.CSQuiz.answer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AlternativeAnswer {
    private Integer alternativeId;
    private Integer answerId;
    private String alternativeText;
}
