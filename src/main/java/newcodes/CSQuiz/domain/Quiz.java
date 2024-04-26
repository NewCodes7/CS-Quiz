package newcodes.CSQuiz.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Quiz {

    private int quizId;
    private int categoryId;
    private String questionText;
    private Difficulty difficulty;
    private String referenceUrl;
    private String blankSentence;

    private int attemptCount;
    private int correctCount;
}

