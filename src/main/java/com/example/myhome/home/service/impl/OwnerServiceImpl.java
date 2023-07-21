package com.example.myhome.home.service.impl;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.ApartmentDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.OwnerService;

import com.example.myhome.home.specification.OwnerSpecifications;
import com.example.myhome.util.FileUploadUtil;
import com.example.myhome.util.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/ownerId/";
    private final OwnerRepository ownerRepository;

    private final ApartmentServiceImpl apartmentService;
    private final FileUploadUtil fileUploadUtil;

    private final ApartmentDTOMapper apartmentDTOMapper;

    @Override
    public Owner findById(Long id) { return ownerRepository.findById(id).orElseThrow(NotFoundException::new);}

    @Override
    public Owner findByLogin(String login) {return ownerRepository.findByEmail(login).orElseThrow(NotFoundException::new);}



    public boolean isOwnerExistsByEmail(String email) {
        Optional<Owner> owner = ownerRepository.findByEmail(email);
        return owner.isPresent();
    }

    @Override
    public Owner save(Owner owner) { return ownerRepository.save(owner); }


    @Override
    public String saveOwnerImage(Long id, MultipartFile file1) throws IOException {
        String fileName = "";
        Owner oldOwner = new Owner();
        if (id!=null) { oldOwner = ownerRepository.getReferenceById(id);
        }
// file1
        if(file1.getSize() > 0) {
            String FileNameUuid = UUID.randomUUID() + "-" + file1.getOriginalFilename();
            fileUploadUtil.saveFile(localPath, FileNameUuid, file1);
            fileName = (localPath + FileNameUuid);
            if(oldOwner.getProfile_picture() != null && oldOwner.getProfile_picture().length() >0) {
            Files.deleteIfExists(Paths.get(uploadPath + oldOwner.getProfile_picture()));
            }
        } else if (oldOwner.getProfile_picture() != null) {
            fileName = oldOwner.getProfile_picture();

        }
    return fileName;
    }

    @Override
    public OwnerDTO findOwnerDTObyEmail(String mail) {
        Owner owner = ownerRepository.findByEmail(mail).orElseThrow();
        return new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name(), (owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name()), apartmentService.convertApartmentsToApartmentsDTO(owner.getApartments()), (long) owner.getUnreadMessages().size(), owner.getProfile_picture());
    }

    @Override
    public OwnerDTO findOwnerDTObyEmailWithMessages(String mail) {
        Owner owner = ownerRepository.findByEmail(mail).orElseThrow();
        return new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name(), (owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name()), apartmentService.convertApartmentsToApartmentsDTO(owner.getApartments()), owner.getMessages(), owner.getPhone_number(), owner.getEmail(), owner.getViber(), owner.getTelegram(), owner.getDescription(), owner.getProfile_picture());
    }

}
