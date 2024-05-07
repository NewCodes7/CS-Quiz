package newcodes.CSQuiz.user.service;

import lombok.Builder;
import newcodes.CSQuiz.user.domain.User;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import newcodes.CSQuiz.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Builder
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (user.getPassword_hashed() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        // 사용자 정보를 UserDetails 객체로 변환하여 반환
        return new CustomUserDetails(user);
    }
}
