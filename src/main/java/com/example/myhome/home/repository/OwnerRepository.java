package com.example.myhome.home.repository;


import com.example.myhome.home.model.Owner;


import com.example.myhome.home.specification.OwnerSpecification;
import com.example.myhome.util.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.time.LocalDate;
public interface OwnerRepository extends JpaRepository<Owner, Long>, JpaSpecificationExecutor<Owner> {
import java.util.Optional;


    Long countAllBy();

    default Page<Owner> findByFilters(Long id, String name, String phoneNumber, String email, String buildingName, Long apartmentNumber, LocalDate added, UserStatus status, Boolean is_debt, Pageable pageable) {
        System.out.println("id "+id+",name "+name+",phoneNumber "+phoneNumber+",email "+email+",buildingName "+buildingName+",apartmentNumber "+apartmentNumber+",added "+added+",status "+status+",is_debt "+is_debt);

        Specification<Owner> spec = Specification.where(null);
        System.out.println("spec1 "+spec);
        if (id != null ) {
            spec = spec.and(OwnerSpecification.idContains(id));
        }
        if (name != null && !name.isEmpty()) {
            spec = spec.and(OwnerSpecification.nameContains(name));
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            spec = spec.and(OwnerSpecification.phonenumberContains(phoneNumber));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(OwnerSpecification.emailContains(email));
        }
      if (buildingName != null && !buildingName.isEmpty() && !buildingName.equalsIgnoreCase("-")) {
          spec = spec.and(OwnerSpecification.buildingContains(buildingName));
      }
        if (apartmentNumber != null) {
            spec = spec.and(OwnerSpecification.apartmentContains(apartmentNumber));
        }
        if (added != null ) {
            spec = spec.and(OwnerSpecification.dateContains(added));
        }
        if (status != null ) {
            spec = spec.and(OwnerSpecification.statusContains(status));
        }
        if (is_debt != null && is_debt == true ) {
            spec = spec.and(OwnerSpecification.hasDebtContains());
        }
        if (is_debt != null && is_debt == false ) {
            spec = spec.and(OwnerSpecification.noDebtContains());
        }
        System.out.println("spec2 "+spec);
        System.out.println("findAll(spec)"+ findAll(spec));
        return findAll(spec, pageable);
    }

    default Page<Owner> findByNameFragment(String name, Pageable pageable) {
        System.out.println("name"+name);
        Specification<Owner> spec = Specification.where(null);
        if (name != null && !name.isEmpty()) {
            spec = spec.and(OwnerSpecification.nameContains(name));
        }
        return findAll(spec, pageable);
    }
    Page<Owner> findAll(Pageable pageable);

//    List<Owner> findByfirst_nameContainingOrlast_nameContainingOrfather_nameContaining(String firstName, String lastName, String fatherName, Pageable pageable);

    Optional<Owner> findByEmail(String email);
    Boolean existsByEmail(String email);
}
