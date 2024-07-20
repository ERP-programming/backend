package ac.su.erp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity  // URL 요청에 대한 Spring Security 동작 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(  // 요청 인가 여부 결정을 위한 조건 판단
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers(
                                                // Apache Ant 스타일 패턴을 사용해 URL 매칭 정의
                                                new AntPathRequestMatcher(
                                                        "/"              // 메인 페이지 비회원 접속 허용
                                                )
                                                // ).denyAll()
                                        ).permitAll()
                                        .requestMatchers(
                                                new AntPathRequestMatcher(
                                                        "/employees/login" // 로그인 URL 비회원 접속 허용
                                                )
                                        ).permitAll().
                                        requestMatchers(
                                                new AntPathRequestMatcher(
                                                        "/css/**" //
                                                )
                                        ).permitAll().
                                        requestMatchers(
                                                new AntPathRequestMatcher(
                                                        "/js/**" //
                                                )
                                        ).permitAll().
                                        requestMatchers(
                                                new AntPathRequestMatcher(
                                                        "/images/**" //
                                                )
                                        ).permitAll()
//                                        .requestMatchers( // GET 으로 상세 화면 진입은 모두 허가
//                                                new AntPathRequestMatcher(
//                                                        "/products-temp/**", HttpMethod.GET.name()
//                                                )
//                                        ).permitAll()
//                                        .requestMatchers( // POST, PUT, DELETE 는 관리자 권한 필요
//                                                new AntPathRequestMatcher(
//                                                        "/products-temp/**"
//                                                )
//                                        )
//                                        .hasAnyRole(
//                                                "HR", "CEO")
                                        .anyRequest().authenticated()  // 나머지 모든 URL 에 회원 로그인 요구
                )
                .csrf(
                        (csrf) ->
                                csrf.ignoringRequestMatchers(
                                        // 필요 시 특정 페이지 CSRF 토큰 무시 설정
                                        new AntPathRequestMatcher("/h2-console/**")
//                                         , new AntPathRequestMatcher("employees/login")
//                                         , new AntPathRequestMatcher("employees/logout")
                                        // , new AntPathRequestMatcher("/signup")
                                )
                )
                .headers(
                        (headers) ->
                                headers.addHeaderWriter(
                                        new XFrameOptionsHeaderWriter(
                                                // X-Frame-Options 는 웹 페이지 내에서 다른 웹 페이지 표시 허용 여부 제어
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN  // 동일 도메인 내에서 표시 허용
                                        )
                                )
                )
                .formLogin(
                        (formLogin) ->
                                formLogin  // Controller 에 PostMapping URL 바인딩이 없어도
                                        // POST 요청을 아래 라인에서 수신하고 인증 처리
                                        .loginPage("/employees/login")
                                        .defaultSuccessUrl("/")
//                AbstractHttpConfigurer::disable
                )
                .logout(
                        (logout) ->
                                logout
                                        .logoutRequestMatcher(new AntPathRequestMatcher("/employees/logout"))
                                        .logoutSuccessUrl("/login?logout")
                                        .invalidateHttpSession(true)
                                        .deleteCookies("JSESSIONID")
                )
//            .sessionManagement(
//                (sessionConfig) -> {
//                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                }
//            )
//            .addFilterBefore(
//                tokenAuthenticationFilter(),  // 토큰을 username, password 검사보다 먼저 검사한다.
//                UsernamePasswordAuthenticationFilter.class
//            )
        ;
        return http.build();
    }


    // passwordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}