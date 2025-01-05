package newcodes.CSQuiz.quiz.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingQuiz {
    private Long id;
    private String requestBody;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
