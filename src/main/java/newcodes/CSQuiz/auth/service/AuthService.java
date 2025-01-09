package newcodes.CSQuiz.auth.service;

import java.util.Optional;
import newcodes.CSQuiz.auth.domain.User;
import newcodes.CSQuiz.auth.dto.AddUserRequest;
import newcodes.CSQuiz.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(AuthRepository authRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authRepository = authRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public int saveAccount(AddUserRequest signupRequest) {
        if (findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 회원입니다.");
        }

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password_hashed(bCryptPasswordEncoder.encode(signupRequest.getPassword_hashed()))
                .build();

        return authRepository.save(user).getUser_id();
    }

    public Optional<User> findByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    public Optional<User> findById(int userId) {
        return authRepository.findById(userId);
    }
}