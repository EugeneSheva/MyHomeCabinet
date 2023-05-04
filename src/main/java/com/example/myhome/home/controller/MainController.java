package com.example.myhome.home.controller;

import com.example.myhome.home.model.pages.AboutPage;
import com.example.myhome.home.service.impl.WebsiteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired private WebsiteServiceImpl websiteService;

    @GetMapping
    public String showMainPage(Model model) {
        model.addAttribute("page", websiteService.getMainPage());
        model.addAttribute("contacts", websiteService.getContactsPage());
        return "main_website/index";
    }

    @GetMapping("/about")
    public String showAboutPage(Model model) {
        AboutPage page = websiteService.getAboutPage();
        List<String> photos = Arrays.stream(page.getPhotos().split(",")).filter((photo) -> !photo.equals("")).collect(Collectors.toList());
        List<String> add_photos = Arrays.stream(page.getAdd_photos().split(",")).filter((photo) -> !photo.equals("")).collect(Collectors.toList());
        List<AboutPage.Document> documents = websiteService.getAllDocuments();
        model.addAttribute("page", page);
        model.addAttribute("photos", photos);
        model.addAttribute("add_photos", add_photos);
        model.addAttribute("documents", documents);
        return "main_website/about";
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("page", websiteService.getServicesPage());
        return "main_website/services";
    }

    @GetMapping("/contacts")
    public String showContactsPage(Model model) {
        model.addAttribute("page", websiteService.getContactsPage());
        return "main_website/contacts";
    }

    @PostMapping("/change-language")
    public String changeLanguage(@RequestParam("language") String language, @RequestParam("url") String url, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        Locale locale = new Locale(language.split("_")[0], language.split("_")[1]);
        localeResolver.setLocale(request, response, locale);
        return "redirect:"+url;
    }

}
