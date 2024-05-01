package newcodes.CSQuiz.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}