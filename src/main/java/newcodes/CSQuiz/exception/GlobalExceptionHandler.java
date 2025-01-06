package newcodes.CSQuiz.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(QuizNotFoundException.class)
    public String handleQuizNotFoundException(QuizNotFoundException e, Model model) {
        model.addAttribute("error", "quizNotFound");
        model.addAttribute("message", e.getMessage());
        return "error/quiz-not-found";
    }
}