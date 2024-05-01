package newcodes.CSQuiz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.domain.Submission;

@Builder
@Getter
@Setter
public class SubmissionDTO {
    private Integer userId;
    private Integer quizId;
    private String submissionDate;
    private Boolean correct;

    public Submission toEntity() {
        return Submission.builder()
                .userId(userId)
                .quizId(quizId)
                .submissionDate(submissionDate) // Null 가능
                .correct(correct)
                .build();
    }
}
