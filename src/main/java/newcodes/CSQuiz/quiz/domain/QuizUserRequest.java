package newcodes.CSQuiz.quiz.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizUserRequest {
    private Long id;
    private String requestBody;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
