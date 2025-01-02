package newcodes.CSQuiz.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.dto.AddUserRequest;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import newcodes.CSQuiz.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) { // form을 전달하는 것이어서 requestbody필요x
        userService.save(request);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                         HttpServletRequest request, HttpServletResponse response,
                         @RequestHeader("Refresh") String refreshToken) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}