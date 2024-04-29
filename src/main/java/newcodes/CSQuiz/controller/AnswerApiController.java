package newcodes.CSQuiz.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.dto.AnswerRequest;
import newcodes.CSQuiz.dto.AnswerResponse;
import newcodes.CSQuiz.service.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping("/quizzes/{id}")
    public String checkAnswer(Model model, @ModelAttribute("answerRequest") AnswerRequest answerRequest) {
        AnswerResponse answerResponse = answerService.check(answerRequest);
        answerResponse.setQuizId(answerRequest.getQuizId());
        answerResponse.setUserId(answerRequest.getUserId());

        model.addAttribute("answerResponse", answerResponse);

        return "/quiz :: #answerResult";
    }
}
