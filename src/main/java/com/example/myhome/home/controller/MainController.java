package com.example.myhome.home.controller;

import com.example.myhome.home.model.pages.AboutPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    @PostMapping("/change-language")
    public @ResponseBody String changeLanguage(@RequestParam("language") String language, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        Locale locale = new Locale(language.split("_")[0], language.split("_")[1]);
        localeResolver.setLocale(request, response, locale);
        System.out.println(request.getRequestURL().toString());
        return "Locale changed to: " + locale.getDisplayLanguage();
    }

}
