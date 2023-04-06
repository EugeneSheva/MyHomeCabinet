package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.pages.*;
import com.example.myhome.home.repository.DocumentRepository;
import com.example.myhome.home.repository.PageRepository;
import com.example.myhome.home.service.WebsiteService;
import com.example.myhome.util.FileUploadUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/website")
@Log
public class WebsiteController {

    @Autowired private WebsiteService websiteService;

    @GetMapping("/home")
    public String showEditHomePage(Model model) {
        model.addAttribute("page", websiteService.getMainPage());
        return "admin_panel/website_settings/website_home";
    }

    @GetMapping("/about")
    public String showEditAboutPage(Model model) {
        AboutPage page = websiteService.getAboutPage();
        List<String> photos = Arrays.stream(page.getPhotos().split(","))
                .filter((photo) -> !photo.equals(""))
                .collect(Collectors.toList());
        List<String> add_photos = Arrays.stream(page.getAdd_photos().split(","))
                .filter((photo) -> !photo.equals(""))
                .collect(Collectors.toList());
        model.addAttribute("page", page);
        model.addAttribute("photos", photos);
        model.addAttribute("add_photos", add_photos);
        model.addAttribute("documents", websiteService.getAllDocuments());
        return "admin_panel/website_settings/website_about";
    }

    @GetMapping("/delete-about-image/{index}")
    public String deleteAboutImage(@PathVariable int index, Model model) {
        AboutPage page = websiteService.getAboutPage();
        page = websiteService.deleteImageAndGetPage(page, index);
        websiteService.savePage(page);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/delete-about-add-image/{index}")
    public String deleteAboutAddImage(@PathVariable int index, Model model) {
        AboutPage page = websiteService.getAboutPage();
        String photos = page.getAdd_photos();
        List<String> photos_l = new ArrayList<>(Arrays.asList(photos.split(",")));
        photos_l.remove(index);
        page.setAdd_photos(String.join(",", photos_l));
        websiteService.savePage(page);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/delete-document/{id}")
    public String deleteDocument(@PathVariable long id) {
        websiteService.deleteDocument(id);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/services")
    public String showEditServicesPage(Model model) {
        model.addAttribute("page", websiteService.getServicesPage());
        return "admin_panel/website_settings/website_services";
    }

    @GetMapping("/contacts")
    public String showEditContactsPage(Model model) {
        model.addAttribute("page", websiteService.getContactsPage());
        return "admin_panel/website_settings/website_contacts";
    }

    // =========================

    @PostMapping("/home")
    public String editHomePage(@ModelAttribute MainPage page,
                               @RequestPart(required = false) MultipartFile page_slide1,
                               @RequestPart(required = false) MultipartFile page_slide2,
                               @RequestPart(required = false) MultipartFile page_slide3,
                               @RequestPart(required = false) MultipartFile page_block_1_img,
                               @RequestPart(required = false) MultipartFile page_block_2_img,
                               @RequestPart(required = false) MultipartFile page_block_3_img,
                               @RequestPart(required = false) MultipartFile page_block_4_img,
                               @RequestPart(required = false) MultipartFile page_block_5_img,
                               @RequestPart(required = false) MultipartFile page_block_6_img) throws IOException {

        page.setId(1);
        page = websiteService.saveMainPageImages(page, page_slide1, page_slide2, page_slide3, page_block_1_img,
                page_block_2_img, page_block_3_img, page_block_4_img, page_block_5_img, page_block_6_img);
        websiteService.savePage(page);

        return "redirect:/admin/website/home";
    }

    @PostMapping("/about")
    public String editAboutPage(@ModelAttribute AboutPage page,
                                @RequestPart(required = false) MultipartFile page_director_photo,
                                @RequestPart(required = false) MultipartFile[] page_photos,
                                @RequestPart(required = false) MultipartFile[] page_add_photos,
                                @RequestParam(required = false) String[] document_names,
                                @RequestParam(required = false) MultipartFile[] document_files) throws IOException {

        page.setId(1);
        page = websiteService.saveAboutPageInfo(page, page_director_photo, page_photos, page_add_photos, document_names, document_files);
        websiteService.savePage(page);

        return "redirect:/admin/website/about";
    }

    @PostMapping("/services")
    public String editServicesPage(@ModelAttribute ServicesPage page,
                                   @RequestParam String[] titles,
                                   @RequestParam String[] descriptions,
                                   @RequestParam MultipartFile[] service_images) {

        page = websiteService.saveServicesPageInfo(page, titles, descriptions, service_images);
        websiteService.savePage(page);

        return "redirect:/admin/website/services";
    }



    @PostMapping("/contacts")
    public String editContactsPage(@ModelAttribute ContactsPage page){
        page.setId(1);
        websiteService.savePage(page);
        return "redirect:/admin/website/contacts";
    }


}
