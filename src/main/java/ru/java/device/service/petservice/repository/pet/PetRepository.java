package ru.java.device.service.petservice.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.java.device.service.petservice.entity.Pet;

import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID>, JpaSpecificationExecutor<Pet> {
}
