package newcodes.CSQuiz.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.auth.dto.AddUserRequest;
import newcodes.CSQuiz.auth.dto.CustomUserDetails;
import newcodes.CSQuiz.auth.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(AddUserRequest request) {
        authService.saveAccount(request);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}