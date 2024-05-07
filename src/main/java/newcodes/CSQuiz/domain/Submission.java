package newcodes.CSQuiz.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Submission {
    private Integer submissionId;
    private Integer userId;
    private Integer quizId;
    private String submissionDate;
    private Boolean correct;
}
