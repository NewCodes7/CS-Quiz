package newcodes.CSQuiz.user.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import newcodes.CSQuiz.user.repository.JdbcTemplateUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JdbcTemplateUserRepository userRepository;

    public JwtLoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                          JdbcTemplateUserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        log.info("인증 필터 내 인증 시도 메서드 작동");
        String email = request.getParameter("email");
        String password = request.getParameter("password_hashed");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                password, Collections.emptyList());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        log.info("인증 필터 내 성공 처리 메서드 작동");

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        Integer userId = userRepository.findByEmail(userDetails.getUsername()).get().getUser_id();

        String accessToken = tokenProvider.generateToken(userId, Duration.ofMinutes(30));
        String refreshToken = tokenProvider.generateToken(userId, Duration.ofDays(7));

        // 응답 본문에 토큰 정보를 포함시키기 위한 Map 생성
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", accessToken);
        tokenMap.put("refresh_token", refreshToken);

        // 응답 본문에 JSON 형식으로 토큰 정보 작성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), tokenMap);

        response.sendRedirect("/quizzes");
    }
}
