package newcodes.CSQuiz.dto;

import lombok.Getter;
import lombok.Setter;
import newcodes.CSQuiz.domain.Difficulty;
import newcodes.CSQuiz.domain.Quiz;

@Getter
@Setter
public class QuizDTO {
    private int categoryId;
    private String questionText;
    private Difficulty difficulty;
    private String referenceUrl;
    private String blankSentence;

    public Quiz toEntity() {
        return Quiz.builder()
                .categoryId(categoryId)
                .questionText(questionText)
                .difficulty(difficulty)
                .referenceUrl(referenceUrl) // Null 가능
                .blankSentence(blankSentence)
                .build();
    }
}