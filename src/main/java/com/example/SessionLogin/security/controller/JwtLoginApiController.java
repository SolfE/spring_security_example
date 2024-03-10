package com.example.SessionLogin.security.controller;

import com.example.SessionLogin.security.dto.JoinRequest;
import com.example.SessionLogin.security.dto.LoginRequest;
import com.example.SessionLogin.security.entitiy.User;
import com.example.SessionLogin.security.jwt.JwtTokenFilter;
import com.example.SessionLogin.security.jwt.JwtTokenUtil;
import com.example.SessionLogin.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.function.ToDoubleBiFunction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginApiController {

    private final UserService userService;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest) {

        if(userService.cheackLoginIdDuplicate(joinRequest.getLoginId())) {
            return "로그인 아이디가 중복됩니다.";
        }

        if(userService.cheackNicknmaeDuplicate(joinRequest.getNickname())) {
            return "닉네임이 중복됩니다";
        }

        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return "비밀번호가 일치하지 않습니다";
        }

        userService.join2(joinRequest);
        return "회원가입 성공";
    }

    // TODO 암호화 비밀번호 로그인 방식으로 변경
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        User user = userService.login(loginRequest);

        if(user == null) {
            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        String secretKey = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
        long expireTimeMs = 1000 * 60 * 60;

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        return String.format("loginId : %s\nnickname : %s\nrole : %s",
                loginUser.getLoginId(), loginUser.getNickname(), loginUser.getRole().name());
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "관리자 페이지 접근 성공";
    }

}
