package newcodes.CSQuiz.submission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.auth.dto.CustomUserDetails;
import newcodes.CSQuiz.exception.validation.SubmissionValidationException;
import newcodes.CSQuiz.exception.validation.ValidationException;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.submission.dto.SubmissionRequest;
import newcodes.CSQuiz.submission.dto.SubmissionResponse;
import newcodes.CSQuiz.submission.service.SubmissionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SubmissionApiController {

    private final SubmissionService submissionService;

    @PostMapping("/quizzes/{id}")
    public String gradeUserSubmission(
            @Valid @ModelAttribute("answerRequest") SubmissionRequest submissionRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            throw new SubmissionValidationException(bindingResult);
        }

        int userId = customUserDetails.getUserId();
        int quizId = submissionRequest.getQuizId();
        submissionRequest.setUserId(userId);

        // 채점 & 결과 저장
        SubmissionResponse submissionResponse = submissionService.gradeSubmission(submissionRequest);
        submissionResponse.setQuizId(quizId);
        submissionResponse.setUserId(userId);

        // 과거 채점 결과 뷰에 전달 (퀴즈 상태에 지금 틀렸어도 과거에 맞았다면 맞았다고 표시)
        Boolean isSolved = submissionService.findById(userId, quizId);
        QuizViewDto quizViewDto = new QuizViewDto();
        quizViewDto.setIsCorrect(isSolved);

        model.addAttribute("quiz", quizViewDto); // 과거 채점까지 고려한 퀴즈 풀이 상태 (O/X)
        model.addAttribute("answerResponse", submissionResponse); // 현재 채점 결과 (O/X)

        return "/quiz :: #answerResult";
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidation(ValidationException e, Model model) {
        model.addAttribute("errors", e.getMessage());
        return "/quiz :: #answerResult";
    }
}
