package newcodes.CSQuiz.user.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.dto.CreateAccessTokenRequest;
import newcodes.CSQuiz.user.dto.CreateAccessTokenResponse;
import newcodes.CSQuiz.user.service.ManagerReissueTokenService;
import newcodes.CSQuiz.user.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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