package ru.java.device.service.petservice.converter;

import lombok.NonNull;
import model.PageRq;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.entity.Pet;
import model.PetRs;
import model.PetFindAllRq;
import model.PetRq;
import ru.java.device.service.petservice.entity.cashe.PetCashe;

import java.sql.Timestamp;
import java.util.Objects;

@Service
public class PetConverter {

    public PetRs map(Pet pet) {
        PetRs rs = new PetRs();

        if (Objects.nonNull(pet)) {
            return new PetRs(
                    pet.getId(),
                    pet.getName(),
                    pet.getAge(),
                    pet.getCreatedAt().toString()
            );
        }
        return rs;
    }

    public Pet mapPetRqToPet(@NonNull PetRq pet) {
        return new Pet(pet.getName(), pet.getAge());
    }

    public PetCashe mapToCashe(Pet pet) {
        PetCashe rs = new PetCashe();

        if (Objects.nonNull(pet)) {
            rs.setId(pet.getId());
            rs.setName(pet.getName());
            rs.setAge(pet.getAge());


            if (Objects.nonNull(pet.getCreatedAt())) {
                rs.setCreatedAt(
                        Timestamp.valueOf(pet.getCreatedAt()).toLocalDateTime()
                );
            }
        }
        return rs;
    }

    public Pet mapCasheToPet(PetCashe pet) {
        Pet rs = new Pet();

        if (Objects.nonNull(pet)) {
            rs.setId(pet.getId());
            rs.setName(pet.getName());
            rs.setAge(pet.getAge());


            if (Objects.nonNull(pet.getCreatedAt())) {
                rs.setCreatedAt(
                        Timestamp.valueOf(pet.getCreatedAt()).toLocalDateTime()
                );
            }
        }
        return rs;
    }

    public PageRequest getPetFindAllRqToPageRequest(PetFindAllRq rq) {
        if (Objects.isNull(rq) || Objects.isNull(rq.getPageRq())) {
            return PageRequest.of(0, 10);
        }

        PageRq pageRq = rq.getPageRq();
        return PageRequest.of(
                Objects.isNull(pageRq.getPageNumber()) || pageRq.getPageNumber() < 0 ? 0 : pageRq.getPageNumber(),
                Objects.isNull(pageRq.getPageSize()) || pageRq.getPageSize() < 1 ? 10 : pageRq.getPageSize()
        );
    }
}
