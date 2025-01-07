package newcodes.CSQuiz.exception.validation;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(String message, BindingResult bindingResult) {
        super(message);
        this.errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}