package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.pages.*;
import com.example.myhome.home.repos.DocumentRepository;
import com.example.myhome.home.repos.PageRepository;
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

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private DocumentRepository documentRepository;

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
        AboutPage page = pageRepository.getAboutPage().orElseGet(AboutPage::new);
        List<String> photos = Arrays.stream(page.getPhotos().split(","))
                .filter((photo) -> !photo.equals(""))
                .collect(Collectors.toList());
        List<String> add_photos = Arrays.stream(page.getAdd_photos().split(","))
                .filter((photo) -> !photo.equals(""))
                .collect(Collectors.toList());
        model.addAttribute("page", page);
        model.addAttribute("photos", photos);
        model.addAttribute("add_photos", add_photos);
        model.addAttribute("documents", documentRepository.findAll());
        return "website_about";
    }

    @GetMapping("/delete-about-image/{index}")
    public String deleteAboutImage(@PathVariable int index, Model model) {
        AboutPage page = pageRepository.getAboutPage().orElseGet(AboutPage::new);
        String photos = page.getPhotos();
        String photoToDelete = photos.split(",")[index];
        photos = Arrays.stream(photos.split(","))
                .filter((photo) -> !photo.equals(photoToDelete))
                .collect(Collectors.joining(","));
        page.setPhotos(photos);
        pageRepository.save(page);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/delete-about-add-image/{index}")
    public String deleteAboutAddImage(@PathVariable int index, Model model) {
        AboutPage page = pageRepository.getAboutPage().orElseGet(AboutPage::new);
        String photos = page.getAdd_photos();
        List<String> photos_l = new ArrayList<>(Arrays.asList(photos.split(",")));
        photos_l.remove(index);
        page.setAdd_photos(String.join(",", photos_l));
        pageRepository.save(page);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/delete-document/{id}")
    public String deleteDocument(@PathVariable long id) {
        documentRepository.deleteById(id);
        return "redirect:/admin/website/about";
    }

    @GetMapping("/services")
    public String showEditServicesPage(Model model) {
        model.addAttribute("page", pageRepository.getServicesPage().orElseGet(ServicesPage::new));
        return "website_services";
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

        MainPage originalPage = pageRepository.getMainPage().orElseGet(MainPage::new);
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
                                @RequestPart(required = false) MultipartFile page_director_photo,
                                @RequestPart(required = false) MultipartFile[] page_photos,
                                @RequestPart(required = false) MultipartFile[] page_add_photos,
                                @RequestParam(required = false) String[] document_names,
                                @RequestParam(required = false) MultipartFile[] document_files) throws IOException {

        AboutPage originalPage = pageRepository.getAboutPage().orElseGet(AboutPage::new);
        log.info(originalPage.toString());
        page.setId(1);

        //Сохранение единичного фото директора
        log.info("Saving director photo");
        if(page_director_photo.getSize() > 0) {
            fileUploadUtil.saveFile(imageSaveDir, page_director_photo.getOriginalFilename(), page_director_photo);
            page.setDirector_photo(page_director_photo.getOriginalFilename());
        } else page.setDirector_photo(originalPage.getDirector_photo());
        log.info("Saved director photo");

        //Сохранение фото из фотогалереи
        log.info("Saving photos...");
        if(page_photos.length > 1) {
            log.info(String.valueOf(page_photos.length));
            log.info("Photos found on page");
            log.info(Arrays.toString(page_photos));
            String photosToSave = "";
            List<String> list = Arrays.stream(originalPage.getPhotos().split(",")).collect(Collectors.toList());
            for(MultipartFile photo : page_photos) {
                log.info("Printing photo info:");
                log.info(photo.toString());
                if(photo.getSize() > 0) {
                    fileUploadUtil.saveFile(imageSaveDir, photo.getOriginalFilename(), photo);
                    list.add(photo.getOriginalFilename());
                }
            }
            photosToSave = String.join(",", list);
            log.info("Saved photos: " + photosToSave);
            page.setPhotos(photosToSave);

        } else if(page_photos.length == 1 && page_photos[0].getSize() > 0) {
            MultipartFile photo = page_photos[0];
            List<String> list = Arrays.stream(originalPage.getPhotos().split(",")).collect(Collectors.toList());
            fileUploadUtil.saveFile(imageSaveDir, photo.getOriginalFilename(), photo);
            log.info("Saved single photo: " + photo.getOriginalFilename());
            page.setPhotos(String.join(",", list) +","+ photo.getOriginalFilename());
        } else page.setPhotos(originalPage.getPhotos());

        log.info(page.toString());
        log.info(page.getPhotos());

        //Сохранение фото из доп.фотогалереи
        log.info("Saving additional photos...");
        if(page_add_photos.length > 1) {
            log.info(String.valueOf(page_add_photos.length));
            log.info("Photos found on page");
            log.info(Arrays.toString(page_add_photos));
            String photosToSave = "";
            List<String> list = Arrays.stream(originalPage.getAdd_photos().split(",")).collect(Collectors.toList());
            for(MultipartFile photo : page_add_photos) {
                log.info("Printing photo info:");
                log.info(photo.toString());
                if(photo.getSize() > 0) {
                    fileUploadUtil.saveFile(imageSaveDir, photo.getOriginalFilename(), photo);
                    list.add(photo.getOriginalFilename());
                }
            }
            photosToSave = String.join(",", list);
            log.info("Saved photos: " + photosToSave);
            page.setAdd_photos(photosToSave);

        } else if(page_add_photos.length == 1 && page_add_photos[0].getSize() > 0) {
            MultipartFile photo = page_add_photos[0];
            List<String> list = Arrays.stream(originalPage.getAdd_photos().split(",")).collect(Collectors.toList());
            fileUploadUtil.saveFile(imageSaveDir, photo.getOriginalFilename(), photo);
            log.info("Saved single photo: " + photo.getOriginalFilename());
            page.setAdd_photos(String.join(",", list) +","+ photo.getOriginalFilename());
        } else page.setAdd_photos(originalPage.getAdd_photos());

        log.info(page.toString());
        log.info(page.getAdd_photos());

        //Сохранение документов
        log.info("Saving documents...");
        for (int i = 1; i < document_names.length; i++) {
            if(document_names[i].isEmpty()) continue;
            AboutPage.Document document = new AboutPage.Document();
            document.setPage(originalPage);
            document.setName(document_names[i]);
            MultipartFile fileToSave = document_files[i];
            if(fileToSave.getSize() > 0) {
                fileUploadUtil.saveFile("/documents/", fileToSave.getOriginalFilename(), fileToSave);
                document.setFile(fileToSave.getOriginalFilename());
            } else continue;
            documentRepository.save(document);
        }

        log.info("Final page info to save: " + page);

        pageRepository.save(page);
        log.info("Saved about page");

        return "redirect:/admin/website/about";
    }

    @PostMapping("/services")
    public String editServicesPage(@ModelAttribute ServicesPage page,
                                   @RequestParam String[] titles,
                                   @RequestParam String[] descriptions,
                                   @RequestParam MultipartFile[] service_images) {
        page.setId(1);
        List<ServicesPage.ServiceDescription> originalList = pageRepository.getServicesPage().orElseGet(ServicesPage::new).getServiceDescriptions();
        page.setServiceDescriptions(new ArrayList<>());
        for (int i = 1; i < titles.length; i++) {
            ServicesPage.ServiceDescription service = new ServicesPage.ServiceDescription();
            service.setTitle(titles[i]);
            service.setDescription(descriptions[i]);
            if(service_images.length > 0) {
                if(service_images[i].getSize() > 0) {
                    try {
                        fileUploadUtil.saveFile(imageSaveDir + "/services/",
                                service_images[i].getOriginalFilename(),
                                service_images[i]);
                        service.setPhoto(service_images[i].getOriginalFilename());
                    } catch(Exception e) {
                        log.info(e.getMessage());
                        log.info(Arrays.toString(e.getStackTrace()));
                        log.info("Can't save image");
                    }
                } else if (i <= originalList.size()) service.setPhoto(originalList.get(i-1).getPhoto());
            }
            page.getServiceDescriptions().add(service);
        }

        pageRepository.save(page);
        return "redirect:/admin/website/services";
    }



    @PostMapping("/contacts")
    public String editContactsPage(@ModelAttribute ContactsPage page){
        page.setId(1);
        pageRepository.save(page);
        return "redirect:/admin/website/contacts";
    }


}
