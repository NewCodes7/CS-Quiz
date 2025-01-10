package newcodes.CSQuiz.quiz.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Quiz {
    private Integer quizId;
    private Integer categoryId;
    private String questionText;
    private String difficulty;
    private String referenceUrl;
    private String blankSentence;

    private int attemptCount;
    private int correctCount;

    private int answerCounts;

    // FIXME: 매개변수 리팩토링 필요
    public void update(int categoryId, String questionText, String difficulty, String referenceUrl,
                       String blankSentence) {
        this.categoryId = categoryId;
        this.questionText = questionText;
        this.difficulty = difficulty;
        this.referenceUrl = referenceUrl;
        this.blankSentence = blankSentence;
    }
}

