package ru.java.device.service.petservice.controller;

import api.RestApi;
import lombok.RequiredArgsConstructor;
import model.PetFindAllPaginRs;
import model.PetFindAllRq;
import model.PetRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.java.device.service.petservice.service.PetService;
import model.PetRs;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PetController implements RestApi {
    private final PetService petService;

    @Override
    public ResponseEntity<PetRs> restApiV1PetPost(UUID idempotentKey, PetRq petRq) {
        PetRs rs = petService.create(petRq, idempotentKey);
        return ResponseEntity.created(
                URI.create(String.format("/rest/api/v1/pets/%s", rs.getId()))
        ).body(rs);
    }

    @Override
    public ResponseEntity<PetFindAllPaginRs> restApiV1PetsPost(PetFindAllRq petFindAllRq) {
        return ResponseEntity.ok(petService.findAll(petFindAllRq));
    }

    @Override
    public ResponseEntity<PetRs> restApiV1PetsIdGet(UUID id) {
        return ResponseEntity.ok(petService.findById(id));
    }
}
