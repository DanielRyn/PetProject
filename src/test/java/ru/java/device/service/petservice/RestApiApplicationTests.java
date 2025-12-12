package ru.java.device.service.petservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.repository.pet.PetRepository;

import java.time.LocalDateTime;

@SpringBootTest
class RestApiApplicationTests {
    @Autowired
    private PetRepository repository;

    @Test
    void contextLoads() {
//        Pet pet = new Pet(
//                "1",
//                1,
//                LocalDateTime.now(),
//                Pet.PetType.CAT
//        );
//        repository.save(pet);
    }
}
