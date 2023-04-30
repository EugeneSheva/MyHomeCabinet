package com.example.myhome.home.controller;

import com.example.myhome.home.configuration.security.CustomUserDetails;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Log
public class LoginController {

    @Autowired PersistentTokenRepository repository;

    @GetMapping("/admin/site/login")
    public String showAdminLoginPage(Model model) {return "main_website/admin_login";}

    @GetMapping("/admin/logout")
    public String adminLogout (HttpServletRequest request, HttpServletResponse response) {
        clearRememberMeCookie(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/admin/site/login";
    }

    void clearRememberMeCookie(HttpServletRequest request, HttpServletResponse response)
    {
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
