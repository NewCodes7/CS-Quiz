package newcodes.CSQuiz.answer.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.dto.AnswerRequest;
import newcodes.CSQuiz.answer.dto.AnswerResponse;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
import newcodes.CSQuiz.answer.dto.SubmissionDTO;
import newcodes.CSQuiz.answer.service.AnswerService;
import newcodes.CSQuiz.answer.service.SubmissionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AnswerApiController {

    private final AnswerService answerService;
    private final SubmissionService submissionService;

    @PostMapping("/quizzes/{id}")
    public String checkAnswer(
            @ModelAttribute("answerRequest") AnswerRequest answerRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model) {
        AnswerResponse answerResponse = answerService.check(answerRequest);
        updateAnswerResponse(answerRequest, answerResponse, customUserDetails, model);
        saveSubmission(answerRequest, answerResponse, customUserDetails);
        updateQuizView(answerRequest, customUserDetails, model);

        return "/quiz :: #answerResult";
    }

    private void updateAnswerResponse(
            AnswerRequest answerRequest,
            AnswerResponse answerResponse,
            CustomUserDetails customUserDetails,
            Model model) {
        answerResponse.setQuizId(answerRequest.getQuizId());
        answerResponse.setUserId(answerRequest.getUserId());
        model.addAttribute("answerResponse", answerResponse);
    }

    private void saveSubmission(
            AnswerRequest answerRequest,
            AnswerResponse answerResponse,
            CustomUserDetails customUserDetails) {
        answerService.save(SubmissionDTO.builder()
                .userId(customUserDetails.getUserId())
                .quizId(answerRequest.getQuizId())
                .correct(answerResponse.getIsAllCorrect())
                .build());
    }

    private void updateQuizView(
            AnswerRequest answerRequest,
            CustomUserDetails customUserDetails,
            Model model) {
        QuizViewDTO quizViewDTO = new QuizViewDTO();
        Boolean isSolved = submissionService.findById(customUserDetails.getUserId(), answerRequest.getQuizId());
        quizViewDTO.setIsCorrect(isSolved);
        model.addAttribute("quiz", quizViewDTO);
    }
}
