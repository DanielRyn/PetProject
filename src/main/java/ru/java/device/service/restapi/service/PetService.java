package ru.java.device.service.restapi.service;

import org.springframework.stereotype.Service;
import model.Pet;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class PetService {
    public Pet find() {
        return getRandomPet();
    }

    public List<Pet> findAll() {
        return Stream.iterate(0, o -> o + 1)
                .limit(23)
                .map(o -> getRandomPet()).toList();
    }

    private Pet getRandomPet() {
        Pet pet = new Pet();
        pet.id(UUID.randomUUID());
        pet.setAge(new Random().nextInt(1, 20));
        pet.setName("Tom_" + new Random().nextInt(1283, 32123));
        pet.setCreatedAt(LocalDateTime.now().toString());

        return pet;
    }
}
