package ru.java.device.service.restapi.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.java.device.service.restapi.Exception.PetNotFoundException;
import ru.java.device.service.restapi.converter.PetConverter;
import ru.java.device.service.restapi.entity.Pet;
import ru.java.device.service.restapi.repository.PetRepository;

import model.PetFindAllPaginRs;
import model.PetFindAllRq;
import model.PetRs;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {
    private final PetRepository repository;
    private final PetConverter petConverter;
    private final ConversionService conversionService;

    public PetFindAllPaginRs findAll(PetFindAllRq rq) {
        PageRequest pageRequest = petConverter.getPetFindAllRqToPageRequest(rq);
        Page<PetRs> paginRs = repository.findAll(pageRequest)
                .map(petConverter::petToPetRs);

        log.info("success findAll pets {}", paginRs
                .map(o -> o.getId().toString()).stream()
                .collect(Collectors.joining(", "))
        );
        return new PetFindAllPaginRs(
                paginRs.getContent(),
                paginRs.getNumber(),
                paginRs.getSize(),
                (int) paginRs.getTotalElements(),
                paginRs.hasNext()
        );
    }

    public PetRs findById(@NonNull UUID petId) {
        PetRs rs = repository.findById(petId)
                .map(petConverter::petToPetRs)
                .orElseThrow(() -> {
                    log.error("pet not found by {}", petId);
                    return new PetNotFoundException(petId);
                });

        log.info("success found pet by {}", petId);
        return rs;
    }
}
