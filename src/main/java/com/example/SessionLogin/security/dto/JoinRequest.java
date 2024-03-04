package com.example.SessionLogin.security.dto;

import com.example.SessionLogin.security.entitiy.User;
import com.example.SessionLogin.security.entitiy.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디는 필수항목입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임은 필수항목입니다.")
    private String nickname;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }

}
