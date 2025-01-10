package newcodes.CSQuiz.common.constant;

public class ErrorMessage {
    private static final String QUIZ_NOT_FOUND_FORMAT = "Quiz not found with id: %d";
    private static final String USER_NOT_FOUND_FORMAT = "User not found with id: %d";
    private static final String INVALID_QUIZ_DATA = "Invalid quiz data";

    public static String quizNotFound(long id) {
        return String.format(QUIZ_NOT_FOUND_FORMAT, id);
    }

    public static String userNotFound(long id) {
        return String.format(USER_NOT_FOUND_FORMAT, id);
    }

    private ErrorMessage() {
        throw new AssertionError("Cannot create instance of constant class");
    }
}