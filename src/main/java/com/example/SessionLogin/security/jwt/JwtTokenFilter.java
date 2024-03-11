package com.example.SessionLogin.security.jwt;

import com.example.SessionLogin.security.entitiy.User;
import com.example.SessionLogin.security.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws SecurityException, IOException, ServletException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더가 없는 경우 쿠키를 헤더로 사용 시도
        if(authorizationHeader == null) {

            if(request.getCookies() == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                            .filter(cookie -> cookie.getName().equals("jwtToken"))
                            .findFirst()
                            .orElse(null);

            if(jwtTokenCookie == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = jwtTokenCookie.getValue();
            authorizationHeader = "Bearer " + jwtToken;
        }

        // 토큰값이 이상한 경우
        if(!authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.split(" ")[1];

        // 토큰이 만료된 경우
        if(JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        String loginId = JwtTokenUtil.getLoginId(token, secretKey);

        User loginUser = userService.getLoginUserByLoginId(loginId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.getLoginId(), null, List.of(new SimpleGrantedAuthority(loginUser.getRole().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
