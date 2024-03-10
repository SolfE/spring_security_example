package com.example.SessionLogin.security.config;


import com.example.SessionLogin.security.auth.MyAccessDeniedHandler;
import com.example.SessionLogin.security.auth.MyAuthenticationEntryPoint;
import com.example.SessionLogin.security.entitiy.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 *    Form Login 방식 설정
 *    SecurityConfig와 SercurityJwtConfig 중 골라서 사용
*/
//@Configuration
//@EnableWebSecurity
// 현재는 JWT을 기본으로 사용

@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/security-login/info").authenticated()
                        .requestMatchers("/security-login/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                        .anyRequest().permitAll()
                )

                .formLogin(login -> login
                    .usernameParameter("loginId")
                    .passwordParameter("password")
                    .loginPage("/security-login/login")
                    .defaultSuccessUrl("/security-login")
                    .failureUrl("/security-login/login")
                )

                .logout(logout -> logout
                        .logoutUrl("/security-login/logout")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                )

                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                        .accessDeniedHandler(new MyAccessDeniedHandler())
                )

                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

                return http.build();
    }
}