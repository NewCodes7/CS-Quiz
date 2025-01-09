package newcodes.CSQuiz.auth.service;

import lombok.Builder;
import newcodes.CSQuiz.auth.domain.User;
import newcodes.CSQuiz.auth.dto.CustomUserDetails;
import newcodes.CSQuiz.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Builder
public class UserDetailService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Autowired
    public UserDetailService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }
}
