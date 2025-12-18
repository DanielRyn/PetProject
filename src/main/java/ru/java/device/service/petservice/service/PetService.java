package ru.java.device.service.petservice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PetsDeleteRs;
import model.PetFindAllPaginRs;
import model.PetFindAllRq;
import model.PetRq;
import model.PetRs;
import model.PetsNotDeleted;
import model.PetsDeleteRq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.exception.badRequest.OperationByIdempotemtKeyWasSuccessException;
import ru.java.device.service.petservice.exception.PetNotFoundException;
import ru.java.device.service.petservice.converter.PetConverter;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.repository.pet.PetRepository;
import ru.java.device.service.petservice.repository.pet.specitifation.PetSpecification;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {
    private final PetRepository repository;
    private final PetConverter petConverter;
    private final PetCasheService petCasheService;
    private final IdempotentService idempotentService;
    private final PetRepository petRepository;

    public PetRs create(@NonNull PetRq rq, @NonNull UUID idempotentKey) {
        if (idempotentService.isExist(idempotentKey)) {
            throw new OperationByIdempotemtKeyWasSuccessException(idempotentKey);
        }

        Pet petToSave = petConverter.mapPetRqToPet(rq);
        Pet saved = repository.save(petToSave);
        idempotentService.add(idempotentKey);

        log.info("success pet saved {}", saved.getId());
        return petConverter.map(saved);
    }

    public void delete(@NonNull UUID petId) {
        if (!petRepository.existsById(petId)) {
            log.info("pet not found by {}", petId);
            throw new PetNotFoundException(petId);
        }

        repository.deleteById(petId);
        petCasheService.delete(petId);
        log.info("pet by {} success deleted", petId);
    }

    public PetsDeleteRs delete(@NonNull PetsDeleteRq rq) {
        List<UUID> successDeleted = new ArrayList<>();
        List<PetsNotDeleted> failedDeleted = new ArrayList<>();

        List<UUID> distinctPetIds = rq.getId().stream().distinct().toList();
        for (UUID petId : distinctPetIds) {
            try {
                delete(petId);
                successDeleted.add(petId);
            } catch (PetNotFoundException e) {
                failedDeleted.add(new PetsNotDeleted(
                        e.getPetId(),
                        e.getMessage()
                ));
            }
        }

        return new PetsDeleteRs(
                successDeleted,
                failedDeleted
        );
    }

    public PetFindAllPaginRs findAll(PetFindAllRq rq) {
        PageRequest pageRequest = petConverter.getPetFindAllRqToPageRequest(rq);
        PetSpecification specification = new PetSpecification(rq);

        Page<PetRs> paginRs = repository
                .findAll(specification, pageRequest)
                .map(petConverter::map);

        log.info("foundAll {} pets", paginRs.getSize());

        return new PetFindAllPaginRs(
                paginRs.getContent(),
                paginRs.getNumber(),
                paginRs.getSize(),
                (int) paginRs.getTotalElements(),
                paginRs.getTotalPages() - 1,
                paginRs.hasNext()
        );
    }

    public PetRs findById(@NonNull UUID petId) {
        Pet rs = petCasheService.get(petId);

        if (Objects.nonNull(rs)) {
            log.info("found pet by {}", petId);
            return petConverter.map(rs);
        }

        rs = repository.findById(petId).orElseThrow(() -> {
            log.info("pet not found by {}", petId);
            return new PetNotFoundException(petId);
        });

        petCasheService.save(rs);
        return petConverter.map(rs);
    }
}
