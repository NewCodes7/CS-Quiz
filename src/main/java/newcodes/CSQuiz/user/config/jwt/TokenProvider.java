package newcodes.CSQuiz.user.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    @Value("${jwt.secret.key}")
    private String jwt_secret_key;

    @Value("${jwt.issuer}")
    private String jwt_issuer;

    public String generateToken(Integer userId, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.getSeconds()), userId);
    }

    private String makeToken(Date expiry, Integer userId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwt_issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(String.valueOf(userId))
//                .claim("id", user.getUser_id()) // id가 필요해??
                .signWith(SignatureAlgorithm.HS256, jwt_secret_key)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwt_secret_key)
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            System.out.println("isValidToken 에러 발생");
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }

    public Integer getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Integer.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwt_secret_key)
                .parseClaimsJws(token)
                .getBody();
    }
}