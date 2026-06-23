package ru.java.device.service.petservice.controller;

import api.RestApi;
import lombok.RequiredArgsConstructor;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.java.device.service.petservice.service.PetService;
import ru.java.device.service.petservice.service.PetTypeService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PetController implements RestApi {
    private final PetService petService;
    private final PetTypeService petTypeService;

    @Override
    public ResponseEntity<PetRs> petCreate(UUID idempotentKey, PetRq petRq) {
        PetRs rs = petService.create(petRq, idempotentKey);
        return ResponseEntity.created(
                URI.create(String.format("/rest/api/v1/pets/%s", rs.getId()))
        ).body(rs);
    }

    @Override
    public ResponseEntity<PetsRs> petCreateSome(UUID idempotentKey, CreatePetsRq createPetsRq) {
        return ResponseEntity.ok(petService.create(createPetsRq, idempotentKey));
    }

    @Override
    public ResponseEntity<PetFindAllPaginRs> petsFindSome(PetFindAllRq petFindAllRq) {
        return ResponseEntity.ok(petService.findAll(petFindAllRq));
    }

    @Override
    public ResponseEntity<PetRs> petsFindById(UUID id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @Override
    public ResponseEntity<PetTypesRs> petsTypesFind() {
        return ResponseEntity.ok(petTypeService.findTypes());
    }

    @Override
    public ResponseEntity<Void> petsDeleteById(UUID id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PetsDeleteRs> petsDeleteByIdsSome(PetsDeleteRq petsDeleteRq) {
        return ResponseEntity
                .status(HttpStatus.MULTI_STATUS)
                .body(petService.delete(petsDeleteRq));
    }
}
