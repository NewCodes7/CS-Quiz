package newcodes.CSQuiz.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import newcodes.CSQuiz.quiz.domain.Quiz;

@Setter
@Getter
@NoArgsConstructor
public class QuizViewDto {

    private Integer quizId;
    private Integer categoryId;
    private String questionText;
    private String difficulty;
    private String referenceUrl;
    private String blankSentence;

    private int attemptCount;
    private int correctCount;

    private int answerCounts;

    private Boolean isCorrect;

    public QuizViewDto(Quiz quiz) {
        this.quizId = quiz.getQuizId();
        this.categoryId = quiz.getCategoryId();
        this.questionText = quiz.getQuestionText();
        this.difficulty = quiz.getDifficulty();
        this.referenceUrl = quiz.getReferenceUrl();
        this.blankSentence = quiz.getBlankSentence();
        this.attemptCount = quiz.getAttemptCount();
        this.correctCount = quiz.getCorrectCount();
        this.answerCounts = quiz.getAnswerCounts();
    }
}