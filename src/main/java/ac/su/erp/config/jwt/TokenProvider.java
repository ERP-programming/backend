package ac.su.erp.config.jwt;

import ac.su.erp.domain.Employee;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;

    // 액세스 토큰 생성
    public String generateAccessToken(Employee employee, Duration expiry) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expiry.toMillis());
        return makeToken(now, expiredAt, employee);
    }

    // JWT 토큰 생성
    private String makeToken(Date now, Date expiredAt, Employee employee) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .setSubject(employee.getEmpName())
                .claim("empNum", employee.getEmpNum())
                // 암호키를 사용해서 시그니처를 작성
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 토큰을 외부에서 수신 후 검증
    public boolean validateToken(String accessToken) {
        // 인코딩된 내용, 암호화된 signature 모두 의미 추출
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 암호키를 알고있는 서버가
                    .parseClaimsJws(accessToken);                      // 토큰을 해석한다
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 수신 후 토큰 소유자 조회
    public Authentication getAuthentication(String accessToken) {
        // 토큰 정보를 통해 유저 인증 정보 확인
        Claims claims = getClaims(accessToken);
        Set<SimpleGrantedAuthority> authorities = Collections.emptySet();
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                accessToken,
                authorities
        );
    }

    // 토큰에서 클레임 추출
    private Claims getClaims(String accessToken) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(accessToken)
                .getBody();
    }
    // Method to get remaining time for accessToken
    public long getRemainingTimeForAccessToken(String accessToken) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getExpiration();
            Date now = new Date();
            return expiration.getTime() - now.getTime();
        } catch (ExpiredJwtException e) {
            return 0; // Token is expired
        } catch (Exception e) {
            throw new RuntimeException("Could not parse token", e);
        }
    }
}
