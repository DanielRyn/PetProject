package ru.java.device.service.petservice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.exception.OperationByIdempotemtKeyWasSuccessException;
import ru.java.device.service.petservice.exception.PetNotFoundException;
import ru.java.device.service.petservice.converter.PetConverter;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.repository.PetRepository;

import model.PetFindAllPaginRs;
import model.PetFindAllRq;
import model.PetRs;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {
    private final PetRepository repository;
    private final PetConverter petConverter;
    private final PetCasheService petCasheService;
    private final IdempotentService idempotentService;

    public PetFindAllPaginRs findAll(PetFindAllRq rq) {
        PageRequest pageRequest = petConverter.getPetFindAllRqToPageRequest(rq);
        Page<PetRs> paginRs = repository.findAll(pageRequest)
                .map(petConverter::map);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("foundAll pets {}", paginRs
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

    public PetRs findById(@NonNull UUID petId, @NonNull UUID idempotentKey) {
        if (idempotentService.isExist(idempotentKey)) {
            throw new OperationByIdempotemtKeyWasSuccessException(idempotentKey);
        }

        Pet rs = petCasheService.get(petId);
        if (Objects.isNull(rs)) {
            rs = repository.findById(petId)
                    .orElseThrow(() -> {
                        log.info("pet not found by {}", petId);
                        return new PetNotFoundException(petId);
                    });

            petCasheService.save(rs);
        }
        idempotentService.add(idempotentKey);

        log.info("found pet by {}", petId);
        return petConverter.map(rs);
    }
}
