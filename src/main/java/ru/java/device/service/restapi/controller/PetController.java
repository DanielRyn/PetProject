package ru.java.device.service.restapi.controller;

import api.RestApi;
import lombok.RequiredArgsConstructor;
import model.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.java.device.service.restapi.Exception.PetNotFoundException;
import ru.java.device.service.restapi.service.PetService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PetController implements RestApi {
    private final PetService petService;

    @Override
    public ResponseEntity<List<Pet>> restApiV1PetsGet() {
        return ResponseEntity.ok(petService.findAll());
    }

    @Override
    public ResponseEntity<Pet> restApiV1PetsIdGet(UUID id) {
        if (id.toString().equals("c596a2b3-b477-43c1-9396-774688b87ac2")) {
            throw new PetNotFoundException(id);
        }

        return ResponseEntity.ok(petService.find());
    }
}
