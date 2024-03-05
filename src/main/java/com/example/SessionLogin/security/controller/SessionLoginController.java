package com.example.SessionLogin.security.controller;

import com.example.SessionLogin.security.dto.JoinRequest;
import com.example.SessionLogin.security.dto.LoginRequest;
import com.example.SessionLogin.security.entitiy.User;
import com.example.SessionLogin.security.entitiy.UserRole;
import com.example.SessionLogin.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login")
public class SessionLoginController {

    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User loginUser = userService.getLoginUserById(userId);

        if (loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        if (userService.cheackLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디 중복"));
        }

        if (userService.cheackNicknmaeDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임 중복"));
        }

        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinReqeust", "passwordCheck", "비밀번호 일치하지 않음"));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.join(joinRequest);
        return "redirect:/session-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
                        HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User user = userService.login(loginRequest);

        if (user == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호 틀림");
        }

        if (bindingResult.hasErrors()) {
            return "login";
        }

        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800);

        return "redirect:/session-login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/session-login";
    }

    @GetMapping("/info")
    public String userInfo(@SessionAttribute(name = "userId", required = false) Long userId, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User loginUser = userService.getLoginUserById(userId);

        if (loginUser == null) {
            return "redirect:/session-login/login";
        }

        model.addAttribute("user", loginUser);
        return "info";
    }

    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "userId", required = false) Long userId, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User loginUser = userService.getLoginUserById(userId);

        if (loginUser == null) {
            return "redirect:/session-login/login";
        }

        if (!loginUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/session-login";
        }

        return "admin";
    }
}
