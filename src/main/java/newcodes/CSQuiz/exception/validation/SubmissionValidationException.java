package newcodes.CSQuiz.exception.validation;

import org.springframework.validation.BindingResult;

public class SubmissionValidationException extends ValidationException {
    public SubmissionValidationException(BindingResult bindingResult) {
        super("제출된 답안이 유효하지 않습니다.", bindingResult);
    }
}