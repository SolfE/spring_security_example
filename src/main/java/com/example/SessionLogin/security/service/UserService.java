package com.example.SessionLogin.security.service;

import com.example.SessionLogin.security.dto.JoinRequest;
import com.example.SessionLogin.security.dto.LoginRequest;
import com.example.SessionLogin.security.entitiy.User;
import com.example.SessionLogin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public boolean cheackLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean cheackNicknmaeDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public void join(JoinRequest request) {
        userRepository.save(request.toEntity());
    }

    public void join2(JoinRequest request) {
        userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
    }

    public User login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByLoginId(request.getLoginId());

        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if(!user.getPassword().equals(request.getPassword())) {
            return null;
        }

        return user;
    }

    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

}
