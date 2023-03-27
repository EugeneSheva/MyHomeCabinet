package com.example.myhome.home.services;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.repos.ApartmentRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/apartment/";
    private final ApartmentRepository apartmentRepository;
    private final FileUploadUtil fileUploadUtil;

    public Apartment findById (Long id) { return apartmentRepository.findById(id).orElseThrow(NotFoundException::new);}

    public List<Apartment> findAll() { return apartmentRepository.findAll(); }

    public Apartment save(Apartment apartment) { return apartmentRepository.save(apartment); }

    public void deleteById(Long id) { apartmentRepository.deleteById(id); }

//    public Building saveBuildindImages(Long id, MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5) throws IOException {
//        Building newBuilding = new Building();
//        Building oldBuilding = new Building();
//        if (id>0) { oldBuilding = buildingRepository.getReferenceById(id);
//            newBuilding.setId(id);
//        }
//// file1
//        if(file1.getSize() > 0) {
//            String FileNameUuid = UUID.randomUUID() + "-" + file1.getOriginalFilename();
//            fileUploadUtil.saveFile(localPath, FileNameUuid, file1);
//            newBuilding.setImg1(localPath + FileNameUuid);
//            if(oldBuilding.getImg1() != null) {
//            Files.deleteIfExists(Paths.get(uploadPath + oldBuilding.getImg1()));
//            }
//        } else if (oldBuilding.getImg1() != null) {
//            newBuilding.setImg1(oldBuilding.getImg1());
//        }
//// file2
//        if(file2.getSize() > 0) {
//            String FileNameUuid = UUID.randomUUID() + "-" + file2.getOriginalFilename();
//            fileUploadUtil.saveFile(localPath, FileNameUuid, file2);
//            newBuilding.setImg2(localPath + FileNameUuid);
//            if(oldBuilding.getImg2() != null) {
//                Files.deleteIfExists(Paths.get(uploadPath + oldBuilding.getImg2()));
//            }
//        } else if (oldBuilding.getImg2() != null) {
//            newBuilding.setImg2(oldBuilding.getImg2());
//        }
//// file3
//        if(file3.getSize() > 0) {
//            String FileNameUuid = UUID.randomUUID() + "-" + file3.getOriginalFilename();
//            fileUploadUtil.saveFile(localPath, FileNameUuid, file3);
//            newBuilding.setImg3(localPath + FileNameUuid);
//            if(oldBuilding.getImg3() != null) {
//                Files.deleteIfExists(Paths.get(uploadPath + oldBuilding.getImg3()));
//            }
//        } else if (oldBuilding.getImg3() != null) {
//            newBuilding.setImg3(oldBuilding.getImg3());
//        }
//// file4
//        if(file4.getSize() > 0) {
//            String FileNameUuid = UUID.randomUUID() + "-" + file4.getOriginalFilename();
//            fileUploadUtil.saveFile(localPath, FileNameUuid, file4);
//            newBuilding.setImg4(localPath + FileNameUuid);
//            if(oldBuilding.getImg4() != null) {
//                Files.deleteIfExists(Paths.get(uploadPath + oldBuilding.getImg4()));
//            }
//        } else if (oldBuilding.getImg4() != null) {
//            newBuilding.setImg4(oldBuilding.getImg4());
//        }
//// file5
//        if(file5.getSize() > 0) {
//            String FileNameUuid = UUID.randomUUID() + "-" + file5.getOriginalFilename();
//            fileUploadUtil.saveFile(localPath, FileNameUuid, file5);
//            newBuilding.setImg5(localPath + FileNameUuid);
//            if(oldBuilding.getImg5() != null) {
//                Files.deleteIfExists(Paths.get(uploadPath + oldBuilding.getImg5()));
//            }
//        } else if (oldBuilding.getImg5() != null) {
//            newBuilding.setImg5(oldBuilding.getImg5());
//        }
//
//        return newBuilding;
//    }


}