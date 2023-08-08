package com.example.myhome.home.service.impl;

import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.ApartmentDTOMapper;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Value("${aws.bucket.name}")
    private String awsBucket;

    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "img/ownerId/";
    @Autowired
    private  OwnerRepository ownerRepository;
    @Autowired
    private  ApartmentServiceImpl apartmentService;
    @Autowired
    private  FileUploadUtil fileUploadUtil;


    public OwnerServiceImpl() {
    }

    @Override
    public Owner findById(Long id) {
        return ownerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Owner findByLogin(String login) {
        return ownerRepository.findByEmail(login).orElseThrow(NotFoundException::new);
    }


    public boolean isOwnerExistsByEmail(String email) {
        Optional<Owner> owner = ownerRepository.findByEmail(email);
        return owner.isPresent();
    }

    @Override
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }


    @Override
    public String saveOwnerImage(Long id, MultipartFile file1) throws IOException {
        String fileName = "";
        Owner oldOwner = new Owner();
        if (id != null) {
            oldOwner = ownerRepository.getReferenceById(id);
        }
// file1
        if (file1.getSize() > 0) {
            String FileNameUuid = UUID.randomUUID() + "-" + file1.getOriginalFilename();
            fileUploadUtil.saveFile(localPath, FileNameUuid, file1);
            fileName = (localPath + FileNameUuid);
            if (oldOwner.getProfile_picture() != null && oldOwner.getProfile_picture().length() > 0) {
                Files.deleteIfExists(Paths.get(uploadPath + oldOwner.getProfile_picture()));
            }
        } else if (oldOwner.getProfile_picture() != null) {
            fileName = oldOwner.getProfile_picture();

        }
        return fileName;
    }

//    @Override
//    public String saveOwnerImageS3(Long id, MultipartFile file1) throws IOException {
//        String fileName = "";
//        Owner oldOwner = new Owner();
//        if (id != null) {
//            oldOwner = ownerRepository.getReferenceById(id);
//        }
//// file1
//        if (file1.getSize() > 0) {
//            String FileNameUuid = UUID.randomUUID() + "-" + file1.getOriginalFilename();
//
//
////            fileUploadUtil.saveFile(localPath, FileNameUuid, file1);
//
//
//            fileUploadUtil.saveFileS3(localPath + FileNameUuid, file1);
//            fileName = ("/" + localPath + FileNameUuid);
//
//            if (oldOwner.getProfile_picture() != null && oldOwner.getProfile_picture().length() > 0) {
////                Files.deleteIfExists(Paths.get(uploadPath + oldOwner.getProfile_picture()));
//                fileUploadUtil.deleteFileS3(oldOwner.getProfile_picture());
//            }
//        } else if (oldOwner.getProfile_picture() != null) {
//            fileName = oldOwner.getProfile_picture();
//
//        }
//        return fileName;
//    }

    @Override
    public OwnerDTO findOwnerDTObyEmail(String mail) {
        Owner owner = ownerRepository.findByEmail(mail).orElseThrow();
        return new OwnerDTO(owner.getId(), owner.getFirst_name(), owner.getLast_name(), owner.getFathers_name(), (owner.getFirst_name() + " " + owner.getLast_name() + " " + owner.getFathers_name()), apartmentService.convertApartmentsToApartmentsDTO(owner.getApartments()), (long) owner.getUnreadMessages().size(), owner.getProfile_picture());
    }

    @Override
    public OwnerDTO findOwnerDTObyEmailWithMessages(String mail) {
        Owner owner = ownerRepository.findByEmail(mail).orElseThrow();
        return new OwnerDTO(owner.getId(), owner.getFirst_name(), owner.getLast_name(), owner.getFathers_name(), (owner.getFirst_name() + " " + owner.getLast_name() + " " + owner.getFathers_name()), apartmentService.convertApartmentsToApartmentsDTO(owner.getApartments()), owner.getMessages(), owner.getPhone_number(), owner.getEmail(), owner.getViber(), owner.getTelegram(), owner.getDescription(), owner.getProfile_picture());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            System.out.println("Start finding");
            System.out.println("login "+ username);
            Optional<Owner> owner = ownerRepository.findByEmail(username);
            System.out.println("Optional owner " + owner);
            if(owner.isEmpty()) {
                System.out.println("Not found");
                throw new UsernameNotFoundException("No user found with the given email");
            }
            return owner.orElseThrow();

    }
}
