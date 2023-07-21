package com.example.myhome.home.service;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.util.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OwnerService {

    Owner findById(Long id);

    Owner findByLogin(String login);

    String saveOwnerImage(Long id, MultipartFile file1) throws IOException;

    OwnerDTO findOwnerDTObyEmail(String mail);

    OwnerDTO findOwnerDTObyEmailWithMessages(String mail);

    Owner save(Owner owner);
    boolean isOwnerExistsByEmail(String email);
}
