package com.example.SessionLogin.security.auth;


import com.example.SessionLogin.security.entitiy.User;
import com.example.SessionLogin.security.entitiy.UserRole;
import com.example.SessionLogin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId(); // 소셜 로그인 주체
        String providerId = oAuth2User.getAttribute("sub"); // 소셜로그인 서비스에서의 식별자
        String loginId = provider + "_" + providerId; // 중복방지용 회원 아이디

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user;

        // 계정이 없다면 회원가입
        if(optionalUser.isEmpty()) {
            user = User.builder()
                    .loginId(loginId)
                    .nickname(oAuth2User.getAttribute("name"))
                    .provider(provider)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        } else {
            // 있다면 조회
            user = optionalUser.get();
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
