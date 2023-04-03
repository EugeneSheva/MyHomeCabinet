package com.example.myhome.home.controller;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.MessageService;
import com.example.myhome.home.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final BuildingService buildingService;
    private final ApartmentRepository apartmentRepository;
    private final ApartmentService apartmentService;
    private final OwnerService ownerService;

    @GetMapping
    public String getMessages(Model model) {
        List<Message> messagesList = messageService.findAll();
        model.addAttribute("messages", messagesList);
        return "admin_panel/messages/messages";
    }

    @GetMapping("/{id}")
    public String getMessage(@PathVariable("id") Long id, Model model) {
        Message message = messageService.findById(id);
        model.addAttribute("message", message);
        return "admin_panel/messages/message";
    }

    @GetMapping("/new")
    public String createMessage(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        List<BuildingDTO> buildingList = buildingService.findAllDTO();
        model.addAttribute("buildings", buildingList);
        return "admin_panel/messages/message_edit";
    }

    @PostMapping("/save")
    public String saveMessage(@ModelAttribute("message") Message message, @RequestParam(name = "debt",
            defaultValue = "false") Boolean debt, @RequestParam(name = "building", defaultValue = "0") Long buildingId,
            @RequestParam(name = "section", defaultValue = "") String section, @RequestParam(name = "floor", defaultValue = "") String floor,
            @RequestParam(name = "apartmentId", defaultValue = "0") Long apartmentId) throws IOException {
//geting recivers
        System.out.println("apartmentId="+apartmentId+" floor="+floor+" section="+section+" building_id="+buildingId+" debt="+debt);
        List<Owner>recivers = new ArrayList<>();
        if(apartmentId==0) {
            if (floor.length() == 0 && section.length() == 0 && buildingId == 0 && debt == false) {
                for (Apartment apartment : apartmentRepository.findAll()) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName("Всем");
            } else if (floor.length() == 0 && section.length() == 0 && buildingId == 0 && debt == true) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBalanceBefore(0D)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName("Всем с задоженностями");
            } else if (floor.length() == 0 && section.length() == 0 && buildingId > 0 && debt == false) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingId(buildingId)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName());
            } else if (floor.length() == 0 && section.length() == 0 && buildingId > 0 && debt == true) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndBalanceBefore(buildingId, 0D)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+" c задолженостями");
            } else if (floor.length() == 0 && section.length() > 0 && buildingId > 0 && debt == false) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCase(buildingId, section)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+", "+section);
            } else if (floor.length() == 0 && section.length() > 0 && buildingId > 0 && debt == true) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndBalanceBefore(buildingId, section,0D)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+", "+section+" c задолженостями");
            } else if (floor.length() > 0 && section.length() == 0 && buildingId > 0 && debt == false) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCase(buildingId, floor)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+", "+floor);
            } else if (floor.length() > 0 && section.length() == 0 && buildingId > 0 && debt == true) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCaseAndBalanceBefore(buildingId, floor,0D)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+", "+floor+" c задолженостями");
            } else if (floor.length() > 0 && section.length() > 0 && buildingId > 0 && debt == false) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCase(buildingId,section, floor)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+", "+section+", "+floor);
            } else if (floor.length() > 0 && section.length() > 0 && buildingId > 0 && debt == true) {
                for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCaseAndBalanceBefore(buildingId,section, floor,0D)) {
                    recivers.add(apartment.getOwner());
                }
                message.setReceiversName(buildingService.findById(buildingId).getName()+", "+section+", "+floor+" c задолженостями");
            }
        } else if (apartmentId>0) { //if we get one selected apartment
            Apartment apartment=apartmentService.findById(apartmentId);
            recivers.add(apartment.getOwner());
            message.setReceiversName(apartment.getBuilding().getName()+", "+apartment.getSection()+", "+apartment.getFloor()+", кв."+apartment.getNumber());
            }
        message.setReceivers(recivers);
        messageService.save(message);
        return "redirect:/messages/";
    }

    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        messageService.deleteById(id);
        return "redirect:/messages/";
    }

    @GetMapping( "/getApartments")
    @ResponseBody
    public List<ApartmentDTO> getApartments(@RequestParam(name = "debt", defaultValue = "false") Boolean debt,
                                                          @RequestParam(name = "building_id", defaultValue = "0") Long buildingId,
                                                          @RequestParam(name = "section", defaultValue = "") String section,
                                                          @RequestParam(name = "floor", defaultValue = "") String floor) {
        System.out.println("getApartments start");
        System.out.println("b="+buildingId+" s="+section.length()+" f="+floor.length()+" d="+debt);
        List<ApartmentDTO>apartmentDTOList = new ArrayList<>();

        if (floor.length()==0 && section.length()==0 && buildingId==0 && debt==false) {
            apartmentDTOList = apartmentService.findDtoApartments();
        } else if (floor.length()==0 && section.length()==0 && buildingId==0 && debt==true){
            apartmentDTOList=apartmentService.findDtoApartmentsWithDebt();
        } else  if (floor.length()==0 && section.length()==0 && buildingId>0 && debt==false){
            apartmentDTOList=apartmentService.findDtoApartmentsByBuilding(buildingId);
        } else  if (floor.length()==0 && section.length()==0 && buildingId>0 && debt==true){
            apartmentDTOList=apartmentService.findDtoApartmentsByBuildingWithDebt(buildingId);
        } else  if (floor.length()==0 && section.length()>0 && buildingId>0 && debt==false){
            apartmentDTOList=apartmentService.findDtoApartmentsByBuildingAndSection(buildingId,section);
        } else  if (floor.length()==0 && section.length()>0 && buildingId>0 && debt==true){
            apartmentDTOList=apartmentService.findDtoApartmentsByBuildingAndSectionWithDebt(buildingId,section);
        } else if (floor.length()>0 && section.length()==0 && buildingId>0 && debt==false) {
            apartmentDTOList = apartmentService.findDtoApartmentsByBuildingAndFloor(buildingId, floor);
        } else if (floor.length()>0 && section.length()==0 && buildingId>0 && debt==true){
            apartmentDTOList=apartmentService.findDtoApartmentsByBuildingAndFloorWithDebt(buildingId,floor);
        } else if (floor.length()>0 && section.length()>0 && buildingId>0 && debt==false){
            apartmentDTOList=apartmentService.findDtoApartmentsByBuildingAndSectionAndFloor(buildingId,section,floor);
        } else if (floor.length()>0 && section.length()>0 && buildingId>0 && debt==true) {
            apartmentDTOList = apartmentService.findDtoApartmentsByBuildingAndSectionAndFloorWithDebt(buildingId, section, floor);
        }

        System.out.println("getApartments fin");
        System.out.println(apartmentDTOList);
        return apartmentDTOList;
    }

    @GetMapping( "/deleteSelected")
    @ResponseBody
    public void deleteSelected(@RequestParam(name = "checkboxList")Long[]checkboxList) {
        System.out.println("deleteSelected start");
        for (Long aLong : checkboxList) {
            messageService.deleteById(aLong);
        }
    }
}

