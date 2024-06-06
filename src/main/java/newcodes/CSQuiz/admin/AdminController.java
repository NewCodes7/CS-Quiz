package newcodes.CSQuiz.admin;

import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.service.QuizUserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private QuizUserRequestService quizRequestService;

    @GetMapping("/admin/quiz-requests")
    public String getQuizRequests(Model model) {
        List<QuizUserRequest> quizRequests = quizRequestService.getPendingQuizRequests();
        model.addAttribute("quizRequests", quizRequests);
        return "admin-quiz-requests";
    }
}
