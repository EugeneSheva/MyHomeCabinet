package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.OwnerDTO;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/owner/";
    private final OwnerRepository ownerRepository;
    private final FileUploadUtil fileUploadUtil;


    public Owner findById (Long id) { return ownerRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public List<Owner> findAll() { return ownerRepository.findAll(); }

    public List<OwnerDTO> findAllDTO() {
        List<OwnerDTO>ownerDTOList=new ArrayList<>();
        for (Owner owner : ownerRepository.findAll()) {
            ownerDTOList.add(new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name()));
        }
        return ownerDTOList;
    }

    public Owner save(Owner owner) { return ownerRepository.save(owner); }

    public void deleteById(Long id) { ownerRepository.deleteById(id); }

    public Long getQuantity() { return ownerRepository.countAllBy();}

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


}
