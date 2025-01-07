package newcodes.CSQuiz.submission.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.submission.dto.SubmissionDto;
import newcodes.CSQuiz.submission.dto.SubmissionRequest;
import newcodes.CSQuiz.submission.dto.SubmissionResponse;
import newcodes.CSQuiz.submission.service.SubmissionService;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SubmissionApiController {

    private final SubmissionService submissionService;

    @PostMapping("/quizzes/{id}")
    public String checkAnswer(
            @ModelAttribute("answerRequest") SubmissionRequest submissionRequest,
            @Valid @ModelAttribute("answerRequest") SubmissionRequest submissionRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model) {
        SubmissionResponse answerResponse = submissionService.check(submissionRequest);
        updateAnswerResponse(submissionRequest, answerResponse, customUserDetails, model);
        saveSubmission(submissionRequest, answerResponse, customUserDetails);
        updateQuizView(submissionRequest, customUserDetails, model);

        return "/quiz :: #answerResult";
    }

    private void updateAnswerResponse(
            SubmissionRequest submissionRequest,
            SubmissionResponse answerResponse,
            CustomUserDetails customUserDetails,
            Model model) {
        answerResponse.setQuizId(submissionRequest.getQuizId());
        answerResponse.setUserId(submissionRequest.getUserId());
        model.addAttribute("answerResponse", answerResponse);
    }

    private void saveSubmission(
            SubmissionRequest submissionRequest,
            SubmissionResponse answerResponse,
            CustomUserDetails customUserDetails) {
        submissionService.save(SubmissionDto.builder()
                .userId(customUserDetails.getUserId())
                .quizId(submissionRequest.getQuizId())
                .correct(answerResponse.getIsAllCorrect())
                .build());
    }

    private void updateQuizView(
            SubmissionRequest submissionRequest,
            CustomUserDetails customUserDetails,
            Model model) {
        QuizViewDto quizViewDTO = new QuizViewDto();
        Boolean isSolved = submissionService.findById(customUserDetails.getUserId(), submissionRequest.getQuizId());
        quizViewDTO.setIsCorrect(isSolved);
        model.addAttribute("quiz", quizViewDTO);
    }
}
