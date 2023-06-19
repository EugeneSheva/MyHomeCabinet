package com.example.myhome.home.controller;

import com.example.myhome.home.configuration.security.CustomUserDetails;
import com.example.myhome.home.dto.newOwnerDTO;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.registration.LoginRequest;
import com.example.myhome.home.service.registration.RegisterService;
import com.example.myhome.home.validator.NewOwnerValidator;
import com.example.myhome.util.UserStatus;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@Log
public class LoginController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private PersistentTokenRepository repository;
    @Autowired
    private NewOwnerValidator newOwnerValidator;
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/cabinet/site/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "main_website/login";
    }

    @GetMapping("/cabinet/site/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("newowner", new newOwnerDTO());
        return "main_website/register";
    }

    @PostMapping("/cabinet/site/register")
    public String registerUser(@Valid @ModelAttribute("newowner") newOwnerDTO newowner,
                               BindingResult bindingResult) {
        newOwnerValidator.validate(newowner, bindingResult);
        if (bindingResult.hasErrors()) {
            return "main_website/register";
        } else {
            Owner owner = new Owner();
            owner.setEnabled(true);
            owner.setStatus(UserStatus.NEW);
            owner.setAdded_at(LocalDateTime.now());
            owner.setFirst_name(newowner.getFirst_name());
            owner.setLast_name(newowner.getLast_name());
            owner.setFathers_name(newowner.getFathers_name());
            owner.setEmail(newowner.getEmail());
            owner.setPassword(BCrypt.hashpw(newowner.getPassword(), BCrypt.gensalt()));
            ownerService.save(owner);
            return "redirect:/cabinet/site/login";
        }
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
