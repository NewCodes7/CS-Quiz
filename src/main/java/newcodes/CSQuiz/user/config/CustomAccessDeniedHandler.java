package newcodes.CSQuiz.user.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 팝업 메시지를 위한 JavaScript 코드
        String popupScript = "<script>alert('Access Denied! You do not have permission to access this resource.');</script>";

//        response.setStatus(HttpStatus.FORBIDDEN.value()); // 403 Forbidden 상태 코드 설정
        response.setContentType("text/html"); // HTML 응답 설정
        response.getWriter().write(popupScript); // 팝업 메시지 JavaScript 코드 전송
        response.getWriter().flush();
    }
}