package com.example.myhome.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class LanguageController {

    @PostMapping("/change-language")
    public String changeLanguage(@RequestParam("language") String language, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        Locale locale = new Locale(language.split("_")[0], language.split("_")[1]);
        localeResolver.setLocale(request, response, locale);
        return "redirect:/admin/statistics/";
    }
}
