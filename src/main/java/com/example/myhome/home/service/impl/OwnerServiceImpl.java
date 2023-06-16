package com.example.myhome.home.service.impl;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.ApartmentDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.impl.ApartmentServiceImpl;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/ownerId/";
    private final OwnerRepository ownerRepository;
    private final BuildingService buildingService;
    private final ApartmentServiceImpl apartmentService;
    private final FileUploadUtil fileUploadUtil;

    private final ApartmentDTOMapper apartmentDTOMapper;



    @Override
    public OwnerDTO findByIdDTO(Long id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new NotFoundException());
        return OwnerDTO.builder()
                .id(owner.getId())
                .first_name(owner.getFirst_name())
                .last_name(owner.getLast_name())
                .fullName(owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name())
                .build();
    }

    @Override
    public Owner findById(Long id) { return ownerRepository.findById(id).orElseThrow(NotFoundException::new);}

    @Override
    public Owner findByLogin(String login) {return ownerRepository.findByEmail(login).orElseThrow(NotFoundException::new);}

    @Override
    public List<Owner> findAll() { return ownerRepository.findAll(); }

    @Override
    public Page<Owner> findAll(Pageable pageable) { return ownerRepository.findAll(pageable); }

    @Override
    public List<ApartmentDTO> findOwnerApartments(Long ownerID) {
        Owner owner = ownerRepository.findById(ownerID).orElseThrow();
        return owner.getApartments().stream().map(apartmentDTOMapper::fromApartmentToDTO).collect(Collectors.toList());
    }

    @Override
    public List<OwnerDTO> findAllDTO() {
        List<OwnerDTO>ownerDTOList=new ArrayList<>();
        for (Owner owner : ownerRepository.findAll()) {
            OwnerDTO newDTO = OwnerDTO.builder()
                                                .id(owner.getId())
                                                .first_name(owner.getFirst_name())
                                                .last_name(owner.getLast_name())
                                                .fullName(owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name())
                                                .build();
            ownerDTOList.add(newDTO);
        }
        return ownerDTOList;
    }

    @Override
    public List<OwnerDTO> getOwnerDTOByPage(String name, int page_number) {

        Pageable pageable = PageRequest.of(page_number, 10);
        return ownerRepository.findByName(name, pageable)
                .stream()
                .map(owner -> new OwnerDTO(
                            owner.getId(),
                            owner.getFirst_name(),
                            owner.getLast_name(),
                            owner.getFathers_name()
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public Page<OwnerDTO> findAllDTO(Pageable pageable) {
        List<OwnerDTO> ownerDTOList = new ArrayList<>();
        Page<Owner> ownerPage = ownerRepository.findAll(pageable);
        for (Owner owner : ownerPage) {
            OwnerDTO newDTO = OwnerDTO.builder()
                    .id(owner.getId())
                    .first_name(owner.getFirst_name())
                    .last_name(owner.getLast_name())
                    .fullName(owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name())
                    .build();
            ownerDTOList.add(newDTO);
        }
        return new PageImpl<>(ownerDTOList, pageable, ownerPage.getTotalElements());
    }

    @Override
    public Page<OwnerDTO> findAllBySpecification(FilterForm filters, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size);

        Page<Owner> initialPage = ownerRepository.findAll(buildSpecFromFilters(filters), pageable);

        List<OwnerDTO> listDTO = initialPage.getContent().stream()
                .map(owner -> {
                    List<BuildingDTO> buildings = new ArrayList<>();
                    List<ApartmentDTO> apartments = new ArrayList<>();
                    String date = (owner.getAdded_at() != null) ? owner.getAdded_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                    String status = (owner.getStatus() != null) ? owner.getStatus().getName() : "";
                    owner.getApartments().forEach(
                            apart -> {
                                buildings.add(BuildingDTO.builder().id(apart.getBuilding().getId()).name(apart.getBuilding().getName()).build());
                                apartments.add(ApartmentDTO.builder().id(apart.getId()).fullName("№"+apart.getNumber()+", " + apart.getBuilding().getName()).build());
                            }
                    );
                    return OwnerDTO.builder()
                            .id(owner.getId())
                            .fullName(owner.getFullName())
                            .phone_number(owner.getPhone_number())
                            .email(owner.getEmail())
                            .buildings(buildings)
                            .apartments(apartments)
                            .date(date)
                            .status(status)
                            .hasDebt(owner.isHas_debt())
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(listDTO, pageable, initialPage.getTotalElements());
    }

    @Override
    public Specification<Owner> buildSpecFromFilters(FilterForm filters) {
        Long id = filters.getId();
        String ownerName = filters.getOwnerName();
        String ownerPhoneNumber = filters.getPhone();
        String email = filters.getEmail();
        Long building = filters.getBuilding();
        Long apartmentNumber = filters.getApartment();
        LocalDate date = (filters.getDate() != null) ? LocalDate.parse(filters.getDate()) : null;
        UserStatus status = (filters.getStatus() != null) ? UserStatus.valueOf(filters.getStatus()) : null;
        Boolean has_debt = filters.getDebt();

        return Specification.where(OwnerSpecifications.idContains(id)
                .and(OwnerSpecifications.nameContains(ownerName))
                .and(OwnerSpecifications.phonenumberContains(ownerPhoneNumber))
                .and(OwnerSpecifications.emailContains(email))
                .and(OwnerSpecifications.hasBuilding(building))
                .and(OwnerSpecifications.apartmentContains(apartmentNumber))
                .and(OwnerSpecifications.dateContains(date))
                .and(OwnerSpecifications.statusContains(status))
                .and(OwnerSpecifications.hasDebt(has_debt)));
    }

    @Override
    public Page<OwnerDTO> findAllBySpecification2(FilterForm filters, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size);
        List<OwnerDTO> listDTO = new ArrayList<>();
        Page<Owner>ownerList = ownerRepository.findByFilters(filters.getId(),filters.getOwnerName(),filters.getPhone(),filters.getEmail(), filters.getBuildingName(),filters.getApartment(), filters.getDate() != null ? LocalDate.parse(filters.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null, filters.getStatus()!=null? UserStatus.valueOf(filters.getStatus()) : null, String.valueOf(filters.getDebtSting()), pageable);
        for (Owner owner : ownerList) {
            List<ApartmentDTO> apartments = new ArrayList<>();
            List<BuildingDTO> buildings = new ArrayList<>();
            owner.getApartments().forEach(
                    apart -> {
                        buildings.add(BuildingDTO.builder().id(apart.getBuilding().getId()).name(apart.getBuilding().getName()).build());
                        apartments.add(ApartmentDTO.builder().id(apart.getId()).fullName("№"+apart.getNumber()+", " + apart.getBuilding().getName()).build());
                    }
            );
            listDTO.add(new OwnerDTO(owner.getId(), owner.getFirst_name(), owner.getLast_name(), owner.getFathers_name(), owner.getFullName(),
                    owner.getPhone_number(), owner.getEmail(), owner.getViber(), owner.getTelegram(), owner.getDescription(), apartments, buildings,
                    owner.getAdded_at().toString(), owner.getStatus().toString(),owner.isHas_debt()));
        }
        return new PageImpl<>(listDTO, pageable, ownerList.getTotalElements());
    }

    @Override
    public Page<OwnerDTO> findByNameFragmentDTO(String name, Pageable pageable) {
        List<OwnerDTO> ownerDTOList = new ArrayList<>();
        Page<Owner> ownerPage = ownerRepository.findByNameFragment(name, pageable);
        for (Owner owner : ownerPage) {
            OwnerDTO newDTO = OwnerDTO.builder()
                    .id(owner.getId())
                    .first_name(owner.getFirst_name())
                    .last_name(owner.getLast_name())
                    .fathers_name(owner.getFathers_name())
                    .fullName(owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name())
                    .build();
            ownerDTOList.add(newDTO);
        }
        return new PageImpl<>(ownerDTOList, pageable, ownerPage.getTotalElements());
    }

    @Override
    public Owner save(Owner owner) { return ownerRepository.save(owner); }

    @Override
    public void deleteById(Long id) { ownerRepository.deleteById(id); }

    @Override
    public Long getQuantity() { return ownerRepository.countAllBy();}

    @Override
    public List<Long> getOwnerApartmentAccountsIds(Long id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(NotFoundException::new);
        return owner.getApartments().stream().map(Apartment::getAccount).map(ApartmentAccount::getId).collect(Collectors.toList());
    }

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
    public Boolean isHaveDebt(String debt) {
        Boolean isDebt = null;
        if (debt.equalsIgnoreCase("haveDebt")) {
            isDebt=true;
        }else if (debt.equalsIgnoreCase("noDebt")) {
            isDebt=false;
        }
        return isDebt;
    }

    @Override
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

    @Override
    public Long countAllOwners() {
        return ownerRepository.count();
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


    @Override
    public OwnerDTO findOwnerDTObyEmailFull(String mail) {
        Owner owner = ownerRepository.findByEmail(mail).orElseThrow();
        return new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name(), (owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name()), apartmentService.convertApartmentsToApartmentsDTO(owner.getApartments()), owner.getMessages(), owner.getPhone_number(),owner.getEmail(),owner.getViber(),owner.getTelegram(), owner.getDescription(), owner.getProfile_picture());
    }

    @Override
    public OwnerDTO convertOwnerToOwnerDTO(Owner owner) {
        return new OwnerDTO(owner.getId(),owner.getFirst_name(),owner.getLast_name(),owner.getFathers_name(), (owner.getFirst_name()+" "+owner.getLast_name()+" "+owner.getFathers_name()), apartmentService.convertApartmentsToApartmentsDTO(owner.getApartments()));
    }


}
