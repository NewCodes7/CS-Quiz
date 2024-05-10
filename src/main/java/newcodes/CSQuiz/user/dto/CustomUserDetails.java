package newcodes.CSQuiz.user.dto;

import newcodes.CSQuiz.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 반환하는 로직을 여기에 작성하세요.
        // 예를 들어, 사용자가 가진 역할을 GrantedAuthority의 리스트로 반환할 수 있습니다.
        // 권한이 없는 경우에는 빈 리스트를 반환하거나 null을 반환할 수 있습니다.
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword_hashed();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Integer getUserId() {
        return user.getUser_id();
    }

    // 아래의 메서드들은 사용하지 않는다면 기본 구현인 빈 메서드로 남겨둘 수 있습니다.
    // 하지만 필요에 따라 추가적인 로직을 구현할 수도 있습니다.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
