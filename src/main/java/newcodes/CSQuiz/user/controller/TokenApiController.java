package newcodes.CSQuiz.user.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.service.ManagerReissueTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/auth")
public class TokenApiController {

    private final ManagerReissueTokenService managerReissueTokenService;

    @GetMapping("/reissue-token")
    public String reissueToken(
            @RequestHeader("Refresh") String refreshToken) {

        // REFACTOR: ResponseEntity 응답
        return managerReissueTokenService.reissueToken(refreshToken);
    }
}