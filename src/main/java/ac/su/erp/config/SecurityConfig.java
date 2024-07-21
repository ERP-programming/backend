package ac.su.erp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // URL 요청에 대한 Spring Security 동작 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    // SecurityFilterChain 빈 설정
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 요청 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        // 홈 페이지는 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        // 로그인 페이지는 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/employees/login")).permitAll()
                        // 공지사항 API는 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/api/notices/**")).permitAll()
                        // 비품 요청 API는 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/api/equipment-requests/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v2/equipment-requests/**")).permitAll()
                        // 프로필 업데이트 페이지는 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/employees/updateProfile/**")).permitAll()
                        // 워크플로우 관련 URL은 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/workflow/**")).permitAll()
                        // CSS 파일은 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        // JavaScript 파일은 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        // 이미지 파일은 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                        // HR 체크는 모든 사용자에게 허용
                        .requestMatchers(new AntPathRequestMatcher("/employees/hr-check")).permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().permitAll()
                )
                // CSRF 설정
                .csrf(csrf -> csrf
                        // 특정 URL에 대해 CSRF 보호 비활성화
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/h2-console/**"), // H2 콘솔
                                new AntPathRequestMatcher("/api/**"), // 모든 API 요청
                                new AntPathRequestMatcher("/workflow/**") // 워크플로우 관련 요청
                        )
                )
                // HTTP 헤더 설정
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                        ))
                )
                // 폼 로그인 설정
                .formLogin(formLogin -> formLogin
                        .loginPage("/employees/login") // 커스텀 로그인 페이지
                        .defaultSuccessUrl("/home") // 로그인 성공 후 리다이렉트 URL
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/employees/logout")) // 로그아웃 URL
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공 후 리다이렉트 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                );

        return http.build();
    }

    // PasswordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 사용
    }

}
