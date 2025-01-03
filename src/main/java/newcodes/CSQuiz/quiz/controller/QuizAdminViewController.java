package newcodes.CSQuiz.quiz.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.service.QuizAdminApiService;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class QuizAdminViewController {

    private final QuizAdminApiService quizRequestService;

    @GetMapping("/admin/quizzes/requests")
    public String getQuizRequests(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<QuizUserRequest> quizRequests = quizRequestService.getPendingQuizRequests();
        model.addAttribute("quizRequests", quizRequests);
        model.addAttribute("loggedIn", customUserDetails != null);

        return "admin-quiz-requests";
    }
}
