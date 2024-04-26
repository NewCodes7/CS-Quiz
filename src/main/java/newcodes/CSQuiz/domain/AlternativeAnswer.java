package newcodes.CSQuiz.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AlternativeAnswer {
    private int alternativeId;
    private int answerId;
    private String alternativeText;
}
