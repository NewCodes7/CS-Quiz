package newcodes.CSQuiz.exception;

import newcodes.CSQuiz.common.constant.ErrorMessage;

public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(int id) {
        super(ErrorMessage.quizNotFound(id));
    }
}