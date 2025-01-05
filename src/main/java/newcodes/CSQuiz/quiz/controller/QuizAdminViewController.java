package newcodes.CSQuiz.quiz.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;
import newcodes.CSQuiz.quiz.service.QuizAdminService;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class QuizAdminViewController {

    private final QuizAdminService quizAdminService;

    @GetMapping("/quizzes/status/pending")
    public String showPendingQuizzes(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<PendingQuiz> quizRequests = quizAdminService.getPendingQuizRequests();
        model.addAttribute("quizRequests", quizRequests);
        model.addAttribute("loggedIn", customUserDetails != null);

        return "admin-quiz-requests";
    }
}
