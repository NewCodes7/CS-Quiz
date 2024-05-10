package newcodes.CSQuiz.user.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import newcodes.CSQuiz.user.config.jwt.TokenProvider;
import newcodes.CSQuiz.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain 실행");

        http
            .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorize ->
                    authorize
                            .requestMatchers("/login", "/signup", "/quizzes", "/").permitAll()
                            .requestMatchers("/new-quiz", "/api/quizzes").hasRole("ADMIN")
                            .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                    formLogin
                            .loginPage("/login")
                            .usernameParameter("email")
                            .passwordParameter("password_hashed")
                            .defaultSuccessUrl("/quizzes")
                            .failureHandler(loginFailHandler())
            )
            .logout(logout ->
                    logout
                            .logoutSuccessUrl("/login")
                            .invalidateHttpSession(true)
            )
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 이거 추가하니 됐다 ㅠㅠ
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailService userService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFailHandler loginFailHandler(){
        return new LoginFailHandler();
    }
}
