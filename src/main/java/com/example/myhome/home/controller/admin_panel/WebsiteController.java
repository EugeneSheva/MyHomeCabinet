package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.pages.*;
import com.example.myhome.home.repos.PageRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/website")
@Log
public class WebsiteController {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    private static final String imageSaveDir = "/pages/";

    @GetMapping("/home")
    public String showEditHomePage(Model model) {
        model.addAttribute("page", pageRepository.getMainPage().orElseGet(MainPage::new));
        return "website_home";
    }

    @GetMapping("/about")
    public String showEditAboutPage(Model model) {
        model.addAttribute("page", pageRepository.getAboutPage().orElseGet(AboutPage::new));;
        return "website_about";
    }

    @GetMapping("/services")
    public String showEditServicesPage(Model model) {
        model.addAttribute("page", pageRepository.getServicesPage().orElseGet(ServicesPage::new));
        return "website_services";
    }

    @GetMapping("/tariffs")
    public String showEditTariffsPage(Model model) {
        model.addAttribute("page", pageRepository.getTariffsPage().orElseGet(TariffsPage::new));
        return "website_tariffs";
    }

    @GetMapping("/contacts")
    public String showEditContactsPage(Model model) {
        model.addAttribute("page", pageRepository.getContactsPage().orElseGet(ContactsPage::new));
        return "website_contacts";
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

        MainPage originalPage = pageRepository.getMainPage().orElseThrow();
        page.setId(1);

        log.info("Setting and saving images for main page...");
        if(!page_slide1.isEmpty() && page_slide1.getOriginalFilename() != null && !page_slide1.getOriginalFilename().equals("")) {
            page.setSlide1(page_slide1.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_slide1.getOriginalFilename(), page_slide1);
        } else page.setSlide1(originalPage.getSlide1());
        if(!page_slide2.isEmpty() && page_slide2.getOriginalFilename() != null && !page_slide2.getOriginalFilename().equals("")) {
            page.setSlide2(page_slide2.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_slide2.getOriginalFilename(), page_slide2);
        } else page.setSlide2(originalPage.getSlide2());
        if(!page_slide3.isEmpty() && page_slide3.getOriginalFilename() != null && !page_slide3.getOriginalFilename().equals("")) {
            page.setSlide3(page_slide3.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_slide3.getOriginalFilename(), page_slide3);
        } else page.setSlide3(originalPage.getSlide3());
        if(!page_block_1_img.isEmpty() && page_block_1_img.getOriginalFilename() != null && !page_block_1_img.getOriginalFilename().equals("")) {
            page.setBlock_1_img(page_block_1_img.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_block_1_img.getOriginalFilename(), page_block_1_img);
        } else page.setBlock_1_img(originalPage.getBlock_1_img());
        if(!page_block_2_img.isEmpty() && page_block_2_img.getOriginalFilename() != null && !page_block_2_img.getOriginalFilename().equals("")) {
            page.setBlock_2_img(page_block_2_img.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_block_2_img.getOriginalFilename(), page_block_2_img);
        } else page.setBlock_2_img(originalPage.getBlock_2_img());
        if(!page_block_3_img.isEmpty() && page_block_3_img.getOriginalFilename() != null && !page_block_3_img.getOriginalFilename().equals("")) {
            page.setBlock_3_img(page_block_3_img.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_block_3_img.getOriginalFilename(), page_block_3_img);
        } else page.setBlock_3_img(originalPage.getBlock_3_img());
        if(!page_block_4_img.isEmpty() && page_block_4_img.getOriginalFilename() != null && !page_block_4_img.getOriginalFilename().equals("")) {
            page.setBlock_4_img(page_block_4_img.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_block_4_img.getOriginalFilename(), page_block_4_img);
        } else page.setBlock_4_img(originalPage.getBlock_4_img());
        if(!page_block_5_img.isEmpty() && page_block_5_img.getOriginalFilename() != null && !page_block_5_img.getOriginalFilename().equals("")) {
            page.setBlock_5_img(page_block_5_img.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_block_5_img.getOriginalFilename(), page_block_5_img);
        } else page.setBlock_5_img(originalPage.getBlock_5_img());
        if(!page_block_6_img.isEmpty() && page_block_6_img.getOriginalFilename() != null && !page_block_6_img.getOriginalFilename().equals("")) {
            page.setBlock_6_img(page_block_6_img.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, page_block_6_img.getOriginalFilename(), page_block_6_img);
        } else page.setBlock_6_img(originalPage.getBlock_6_img());

        pageRepository.save(page);
        log.info("Saved main page");

        return "redirect:/admin/website/home";
    }

    @PostMapping("/about")
    public String editAboutPage(@ModelAttribute AboutPage page,
                                @RequestPart(required = false) MultipartFile director_photo,
                                @RequestPart(required = false) MultipartFile[] photos,
                                @RequestPart(required = false) MultipartFile[] add_photos) throws IOException {

        AboutPage originalPage = pageRepository.getAboutPage().orElseGet(AboutPage::new);
        page.setId(1);

        log.info("Saving director photo");
        if(!director_photo.isEmpty() && director_photo.getOriginalFilename() != null && !director_photo.getOriginalFilename().equals("")) {
            page.setDirector_photo(director_photo.getOriginalFilename());
            fileUploadUtil.saveFile(imageSaveDir, director_photo.getOriginalFilename(), director_photo);
        }
        log.info("Saving photos...");
        if(photos.length != 0) {
            List<String> list = new ArrayList<>();
            for(MultipartFile photo : photos) {
                list.add(photo.getOriginalFilename());
                fileUploadUtil.saveFile(imageSaveDir, photo.getOriginalFilename(), photo);
            }
            page.setPhotos(list);
        }
        log.info("Saving additional photos...");
        if(add_photos.length != 0) {
            List<String> list = new ArrayList<>();
            for(MultipartFile photo : add_photos) {
                list.add(photo.getOriginalFilename());
                fileUploadUtil.saveFile(imageSaveDir, photo.getOriginalFilename(), photo);
            }
            page.setPhotos(list);
        }

        pageRepository.save(page);
        log.info("Saved about page");

        return "redirect:/admin/website/about";
    }

    @PostMapping("/services")
    public String editServicesPage(@ModelAttribute ServicesPage page) {
        page.setId(1);
        pageRepository.save(page);
        return "redirect:/admin/website/services";
    }

    //ТАРИФЫ ДОДЕЛАТЬ

    @PostMapping("/contacts")
    public String editContactsPage(@ModelAttribute ContactsPage page){
        page.setId(1);
        pageRepository.save(page);
        return "redirect:/admin/website/contacts";
    }


}
