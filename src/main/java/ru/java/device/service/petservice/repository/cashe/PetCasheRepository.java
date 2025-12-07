package ru.java.device.service.petservice.repository.cashe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.java.device.service.petservice.entity.cashe.PetCashe;

import java.util.UUID;

@Repository
public interface PetCasheRepository extends CrudRepository<PetCashe, UUID> {

}
