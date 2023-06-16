package com.example.myhome.home.controller;

import com.example.myhome.home.configuration.security.CustomUserDetails;
import com.example.myhome.home.service.registration.LoginRequest;
import com.example.myhome.home.service.registration.RegistrationRequest;
import com.example.myhome.home.service.registration.RegisterService;
import com.example.myhome.home.validator.LoginRequestValidator;
import com.example.myhome.home.validator.RegistrationRequestValidator;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Log
public class LoginController {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private RegistrationRequestValidator validator;
    @Autowired
    private LoginRequestValidator loginRequestValidator;

    @Autowired
    private PersistentTokenRepository repository;

    @GetMapping("/cabinet/site/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "main_website/login";
    }

//    @PostMapping("/cabinet/site/login")
//    public String logInUser(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
//                            Model model,
//                            HttpServletRequest request) {
//        loginRequestValidator.validate(loginRequest, bindingResult);
//        if (bindingResult.hasErrors()) {
//            log.info("Errors found in login request");
//            log.info(bindingResult.getAllErrors().toString());
//            System.out.println("error!!!");
//            return "main_website/login";
//        }
//        System.out.println("ok!!!");
//        return "redirect:/cabinet";
//    }

//    @GetMapping("/admin/site/login")
//    public String showAdminLoginPage(Model model) {
//        return "main_website/admin_login";
//    }

    @GetMapping("/cabinet/site/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "main_website/register";
    }

    @PostMapping("/cabinet/site/register")
    public String registerUser(@ModelAttribute RegistrationRequest registrationRequest,
                               BindingResult bindingResult,
                               HttpServletRequest request) {
        validator.validate(registrationRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("Errors found with reg.request");
            log.info(bindingResult.getAllErrors().toString());
            return "main_website/register";
        }
        registerService.register(registrationRequest);

        return "redirect:/cabinet/site/login";
    }

    @GetMapping("/cabinet/site/register/confirm")
    public String confirmRegister(@RequestParam String token) {
        log.info(token);
        if (token == null || !registerService.confirm(token)) {
            log.info("Wrong token, try again!");
        }
        return "redirect:/cabinet/site/login";
    }

    @GetMapping("/cabinet/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        clearRememberMeCookie(request, response);
//        clearRememberMeTokens();
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/cabinet/site/login";
    }

//    @GetMapping("/admin/logout")
//    public String adminLogout(HttpServletRequest request, HttpServletResponse response) {
//        clearRememberMeCookie(request, response);
//        SecurityContextHolder.getContext().setAuthentication(null);
//        return "redirect:/admin/site/login";
//    }

    void clearRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
        String cookieName = "remember-me";
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request.getContextPath() : "/");
        response.addCookie(cookie);
    }

    void clearRememberMeTokens() {
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = details.getUsername();
        repository.removeUserTokens(username);
    }

}
