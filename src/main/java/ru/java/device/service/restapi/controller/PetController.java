package ru.java.device.service.restapi.controller;

import api.RestApi;
import lombok.RequiredArgsConstructor;
import model.PetFindAllPaginRs;
import model.PetFindAllRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.java.device.service.restapi.service.PetService;
import model.PetRs;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PetController implements RestApi {
    private final PetService petService;

    @Override
    public ResponseEntity<PetFindAllPaginRs> restApiV1PetsGet(PetFindAllRq petFindAllRq) {
        return ResponseEntity.ok(petService.findAll(petFindAllRq));
    }

    @Override
    public ResponseEntity<PetRs> restApiV1PetsIdGet(UUID id) {
        return ResponseEntity.ok(petService.findById(id));
    }
}
