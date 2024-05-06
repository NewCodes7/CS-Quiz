package newcodes.CSQuiz.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerRequest;
import newcodes.CSQuiz.dto.AnswerResponse;
import newcodes.CSQuiz.dto.CustomUserDetails;
import newcodes.CSQuiz.dto.QuizViewDTO;
import newcodes.CSQuiz.dto.SubmissionDTO;
import newcodes.CSQuiz.service.AnswerService;
import newcodes.CSQuiz.service.SubmissionService;
import org.springframework.security.core.Authentication;
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
            Model model, @ModelAttribute("answerRequest") AnswerRequest answerRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AnswerResponse answerResponse = answerService.check(answerRequest);
        answerResponse.setQuizId(answerRequest.getQuizId());
        answerResponse.setUserId(answerRequest.getUserId());

        answerService.save(SubmissionDTO.builder()
                        .userId(customUserDetails.getUserId())
                        .quizId(answerRequest.getQuizId())
                        .correct(answerResponse.getIsAllCorrect())
                        .build());

        model.addAttribute("answerResponse", answerResponse);

        // unsolved -> solved 비동기식 처리
        Boolean isSolved = submissionService.findById(customUserDetails.getUserId(), answerRequest.getQuizId());
        QuizViewDTO quizViewDTO = new QuizViewDTO();
        quizViewDTO.setIsCorrect(isSolved);

        model.addAttribute("quiz", quizViewDTO);

        return "/quiz :: #answerResult";
    }
}
