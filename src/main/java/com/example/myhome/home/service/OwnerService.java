package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.util.FileUploadUtil;
import com.example.myhome.util.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {
    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/owner/";
    private final OwnerRepository ownerRepository;
    private final AccountRepository accountRepository;
    private final FileUploadUtil fileUploadUtil;


    public Owner findById (Long id) { return ownerRepository.findById(id).orElseThrow(() -> new NotFoundException());}
    public OwnerDTO findByIdDTO (Long id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new NotFoundException());
    return new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name(), (owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name()));}

    public List<Owner> findAll() { return ownerRepository.findAll(); }
    public Page<Owner> findAll(Pageable pageable) { return ownerRepository.findAll(pageable); }

    public List<OwnerDTO> findAllDTO() {
        List<OwnerDTO>ownerDTOList=new ArrayList<>();
        for (Owner owner : ownerRepository.findAll()) {
            ownerDTOList.add(new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name(), (owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name())));
        }
        return ownerDTOList;
    }

    public Page<OwnerDTO> findAllDTO(Pageable pageable) {
        List<OwnerDTO> ownerDTOList = new ArrayList<>();
        Page<Owner> ownerPage = ownerRepository.findAll(pageable);
        for (Owner owner : ownerPage) {
            ownerDTOList.add(new OwnerDTO(owner.getId(), owner.getFirst_name(), owner.getLast_name(), owner.getFathers_name(), (owner.getFirst_name() + " " + owner.getLast_name() + " " + owner.getFathers_name())));
        }
        return new PageImpl<>(ownerDTOList, pageable, ownerPage.getTotalElements());
    }


    public Page<OwnerDTO> findByNameFragmentDTO(String name, Pageable pageable) {
        List<OwnerDTO> ownerDTOList = new ArrayList<>();
        Page<Owner> ownerPage = ownerRepository.findByNameFragment(name, pageable);
        for (Owner owner : ownerPage) {
            ownerDTOList.add(new OwnerDTO(owner.getId(), owner.getFirst_name(), owner.getLast_name(), owner.getFathers_name(), (owner.getFirst_name() + " " + owner.getLast_name() + " " + owner.getFathers_name())));
        }
        return new PageImpl<>(ownerDTOList, pageable, ownerPage.getTotalElements());
    }

    public Owner save(Owner owner) { return ownerRepository.save(owner); }

    public void deleteById(Long id) { ownerRepository.deleteById(id); }

    public Long getQuantity() { return ownerRepository.countAllBy();}

    public List<Long> getOwnerApartmentAccountsIds(Long id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(NotFoundException::new);
        return owner.getApartments().stream().map(Apartment::getAccount).map(ApartmentAccount::getId).collect(Collectors.toList());
    }

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
            if(oldOwner.getProfile_picture() != null) {
            Files.deleteIfExists(Paths.get(uploadPath + oldOwner.getProfile_picture()));

            }
        } else if (oldOwner.getProfile_picture() != null) {
            fileName = oldOwner.getProfile_picture();

        }
    return fileName;
    }
    public Boolean isHaveDebt(String debt) {
        Boolean isDebt = null;
        if (debt.equalsIgnoreCase("haveDebt")) {
            isDebt=true;
        }else if (debt.equalsIgnoreCase("noDebt")) {
            isDebt=false;
        }
        return isDebt;
    }

    public UserStatus stringStatusConverter(String status) {
        UserStatus userstatus = null;
        if (status.equalsIgnoreCase("active")) {
            userstatus = UserStatus.ACTIVE;
        }else if (status.equalsIgnoreCase("new")) {
            userstatus = UserStatus.NEW;
        }else if (status.equalsIgnoreCase("disabled")) {
            userstatus = UserStatus.DISABLED;
        }
        return userstatus;
    }

//    public Page<OwnerDTO> findAllDTO(Pageable pageable) {
//        Page<OwnerDTO>ownerDTOList = null;
//        for (Owner owner : ownerRepository.findAll()) {
//            ownerDTOList.add(new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name()));
//        }
//        return ownerDTOList;
//    }

}
