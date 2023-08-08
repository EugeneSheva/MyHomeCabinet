package com.example.myhome.home.controller;

import com.example.myhome.home.dto.newOwnerDTO;
import com.example.myhome.home.model.ForgotPasswordToken;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.repository.ForgotPasswordTokenRepository;
import com.example.myhome.home.service.EmailService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.registration.LoginRequest;
import com.example.myhome.home.service.registration.RegisterService;

import com.example.myhome.home.validator.NewOwnerValidator;
import com.example.myhome.util.UserStatus;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@Log

public class LoginController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private NewOwnerValidator newOwnerValidator;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ForgotPasswordTokenRepository passwordTokenRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping("/cabinet/site/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "main_website/login";
    }

//    @PostMapping("/cabinet/site/login")
//    public String login(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
//            @RequestParam(name = "remember-me", required = false) boolean rememberMe,
//            Model model
//    ) {
//        System.out.println("TEST");
//        System.out.println("rememberMe "+ rememberMe);
//
//        // аутентификация
//
////        if(rememberMe) {
////            authentication.setRememberMe(true);
////        }
//
//        return "redirect:/";
//    }

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

    @GetMapping("/cabinet/forgot-password")
    public String showForgotPasswordPage(@RequestParam(required = false) String token) {
        if (token != null) {
            if (passwordTokenRepository.existsByToken(token)) {
                ForgotPasswordToken foundToken = passwordTokenRepository.findByToken(token).get();
                Owner owner = ownerService.findByLogin(foundToken.getEmail());
                return "redirect:/cabinet/reset-password?id=" + owner.getId() + "&token=" + token;
            } else return "main_website/admin_login";
        }
        return "main_website/forgot-password";
    }

    @PostMapping("/cabinet/forgot-password")
    public String forgotPasswordHandler(@RequestParam String username, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String trimmedEmail = username.trim();

        System.out.println("ownerService.isOwnerExistsByEmail(trimmedEmail)  " + ownerService.isOwnerExistsByEmail(trimmedEmail));
        if (ownerService.isOwnerExistsByEmail(trimmedEmail)) {
            ForgotPasswordToken token = new ForgotPasswordToken(UUID.randomUUID().toString());
            token.setEmail(ownerService.findByLogin(trimmedEmail).getEmail());
            passwordTokenRepository.save(token);
            String emailContent = "<a href=\"" + request.getRequestURL() + "?token=" + token.getToken() + "\">Click on the link to reset your password</a>(something wrong w/ text encoding for cyrillic)";
            try {
                emailService.send(trimmedEmail, emailContent);
                redirectAttributes.addFlashAttribute("success", "Письмо отправлено на указанную почту!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Ошибка при отправке письма на заданную почту!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Пользователя с такой почтой не существует!");

        }
        return "redirect:/cabinet/forgot-password";
    }

    @GetMapping("/cabinet/reset-password")
    public String showResetPasswordPage(@RequestParam(required = false) Long id,
                                        @RequestParam(required = false) String token,
                                        Model model) {
        if(id == null ||
                token == null ||
                !passwordTokenRepository.existsByToken(token) ||
                ownerService.findById(id) == null) return "redirect:/cabinet";

        if(!passwordTokenRepository.findByToken(token).get().getEmail()
                .equals(ownerService.findById(id).getEmail())) return "redirect:/cabinet";

        model.addAttribute("id", id);
        model.addAttribute("token", token);
        return "main_website/reset-password";
    }

    @PostMapping("/cabinet/reset-password")
    public String resetPasswordHandler(@RequestParam Long id,
                                       @RequestParam String token,
                                       @RequestParam(required = false) String password,
                                       @RequestParam(required = false) String confirm_password,
                                       RedirectAttributes redirectAttributes) {
        //trim password
        if(password != null && confirm_password != null) {
            password = password.trim();
            confirm_password = confirm_password.trim();
        }
        //validation
        if(password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Введите пароль!");
            return "redirect:/cabinet/reset-password?id="+id+"&token="+token;
        } else if(password.length() < 8 || password.length() > 100) {
            redirectAttributes.addFlashAttribute("error", "Длина пароля: 8-100");
            return "redirect:/cabinet/reset-password?id="+id+"&token="+token;
        } else if(confirm_password == null || confirm_password.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Подтвердите пароль!");
            return "redirect:/cabinet/reset-password?id="+id+"&token="+token;
        } else if(!confirm_password.equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Пароли не совпадают!");
            return "redirect:/cabinet/reset-password?id="+id+"&token="+token;

        }
        //reset password and delete token if successful
        Owner owner = ownerService.findById(id);
        owner.setPassword(passwordEncoder.encode(password));
        ownerService.save(owner);
        passwordTokenRepository.findByToken(token).ifPresent(passwordTokenRepository::delete);
        redirectAttributes.addFlashAttribute("password_reset", "Пароль был успешно изменен!");
        return "redirect:/cabinet/site/login";
    }

}
