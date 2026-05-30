package ru.java.device.service.petservice.converter;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.entity.PetType;
import ru.java.device.service.petservice.entity.cashe.PetCashe;

import java.sql.Timestamp;
import java.util.Objects;

@Service
public class PetConverter {

    public model.PetRs mapPetToPetRs(Pet pet) {
        model.PetRs rs = new model.PetRs();

        if (Objects.nonNull(pet)) {
            return new model.PetRs(
                    pet.getId(),
                    pet.getName(),
                    pet.getAge(),
                    pet.getPetType().toString(),
                    pet.getCreatedAt().toString()
            );
        }
        return rs;
    }

    public Pet mapPetRqToPet(@NonNull model.PetRq pet) {
        return new Pet(
                pet.getName(),
                pet.getAge(),
                PetType.get(pet.getType())
        );
    }

    public PetCashe mapPetToPetCashe(Pet pet) {
        PetCashe rs = new PetCashe();

        if (Objects.nonNull(pet)) {
            rs.setId(pet.getId());
            rs.setName(pet.getName());
            rs.setAge(pet.getAge());
            rs.setType(pet.getPetType());
            rs.setIsDeleted(pet.getIsDeleted());

            if (Objects.nonNull(pet.getCreatedAt())) {
                rs.setCreatedAt(
                        Timestamp.valueOf(pet.getCreatedAt()).toLocalDateTime()
                );
            }

            if (Objects.nonNull(pet.getDeletedAt())) {
                rs.setDeletedAt(
                        Timestamp.valueOf(pet.getDeletedAt()).toLocalDateTime()
                );
            }
        }
        return rs;
    }

    public Pet mapPetCasheToPet(PetCashe pet) {
        Pet rs = new Pet();

        if (Objects.nonNull(pet)) {
            rs.setId(pet.getId());
            rs.setName(pet.getName());
            rs.setAge(pet.getAge());
            rs.setPetType(pet.getType());
            rs.setIsDeleted(pet.getIsDeleted());

            if (Objects.nonNull(pet.getCreatedAt())) {
                rs.setCreatedAt(
                        Timestamp.valueOf(pet.getCreatedAt()).toLocalDateTime()
                );
            }

            if (Objects.nonNull(pet.getDeletedAt())) {
                rs.setDeletedAt(
                        Timestamp.valueOf(pet.getDeletedAt()).toLocalDateTime()
                );
            }
        }
        return rs;
    }
}
