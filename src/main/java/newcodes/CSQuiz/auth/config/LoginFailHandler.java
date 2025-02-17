package newcodes.CSQuiz.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    //httpServletRequest -> request 에 대한 정보 , httpServletResponse -> response 에 대해 설정할 수 있는 변수
    //AuthenticationException e -> 로그인 실패 시 예외에 대한 정보를 담고 있음.
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {

        // REFACTOR: 예외처리 핸들러 일관성있게..(이거 왜 이렇게 했는지?)
        String errorMessage;
        if (e instanceof BadCredentialsException || e instanceof InternalAuthenticationServiceException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다.";
        } else if (e instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 아이디입니다.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인이 안 되고 있습니다.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }

}
