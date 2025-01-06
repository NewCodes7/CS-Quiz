package newcodes.CSQuiz.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleQuizNotFoundException(QuizNotFoundException e, Model model) {
        model.addAttribute("error", "quizNotFound");
        model.addAttribute("message", e.getMessage());
        return "error/quiz-not-found";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationExceptions(MethodArgumentNotValidException e, Model model) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        model.addAttribute("error", "Validation error occurred");
        model.addAttribute("message", errors);

        return "error/general-page";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception e, Model model) {
        model.addAttribute("error", "An unexpected error occurred");
        model.addAttribute("message", e.getMessage());
        return "error/general-page";
    }
}