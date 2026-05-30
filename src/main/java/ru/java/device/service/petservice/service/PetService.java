package ru.java.device.service.petservice.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.util.PetFindAllFilterUtil;
import ru.java.device.service.petservice.exception.badRequest.OperationByIdempotemtKeyWasSuccessException;
import ru.java.device.service.petservice.exception.PetNotFoundException;
import ru.java.device.service.petservice.converter.PetConverter;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.repository.pet.PetRepository;
import ru.java.device.service.petservice.repository.pet.specitifation.PetSpecification;

import java.time.LocalDateTime;
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

    public model.PetRs create(@NonNull model.PetRq rq, @NonNull UUID idempotentKey) {
        if (idempotentService.isExist(idempotentKey)) {
            throw new OperationByIdempotemtKeyWasSuccessException(idempotentKey);
        }

        Pet petToSave = petConverter.mapPetRqToPet(rq);
        Pet saved = repository.save(petToSave);
        idempotentService.add(idempotentKey);

        log.info("pet was save {}", saved.getId());
        return petConverter.mapPetToPetRs(saved);
    }

    public model.PetsRs create(@NonNull model.CreatePetsRq rq, @NonNull UUID idempotentKey) {
        if (idempotentService.isExist(idempotentKey)) {
            throw new OperationByIdempotemtKeyWasSuccessException(idempotentKey);
        }

        List<model.PetRs> petRs = new ArrayList<>();
        for (model.PetRq petRq : rq.getPetsRq()) {

            Pet petToSave = petConverter.mapPetRqToPet(petRq);
            Pet saved = repository.save(petToSave);
            petRs.add(petConverter.mapPetToPetRs(saved));
        }

        idempotentService.add(idempotentKey);

        log.info("was save {} pets", petRs.size());
        return new model.PetsRs(petRs);
    }

    @Transactional
    public void delete(@NonNull UUID petId) {
        if (!petRepository.existsPetByIsDeletedAndById(petId)) {
            log.info("pet not found by {}", petId);
            throw new PetNotFoundException(petId);
        }

        repository.setDeletedStatusById(petId, LocalDateTime.now());
        petCasheService.delete(petId);
        log.info("pet by {} success deleted", petId);
    }

    @Transactional
    public model.PetsDeleteRs delete(@NonNull model.PetsDeleteRq rq) {
        List<UUID> successDeleted = new ArrayList<>();
        List<model.PetsNotDeleted> failedDeleted = new ArrayList<>();

        List<UUID> distinctPetIds = rq.getId().stream().distinct().toList();
        for (UUID petId : distinctPetIds) {
            try {
                delete(petId);
                successDeleted.add(petId);
            } catch (Exception e) {
                failedDeleted.add(new model.PetsNotDeleted(
                        petId,
                        e.getMessage()
                ));
            }
        }

        return new model.PetsDeleteRs(
                successDeleted,
                failedDeleted
        );
    }

    public model.PetFindAllPaginRs findAll(model.PetFindAllRq rq) {
        Page<model.PetRs> petFoundAllPaginRs = repository
                .findAll(
                        new PetSpecification(rq),
                        PetFindAllFilterUtil.getPetFindAllRqToPageRequest(rq)
                ).map(petConverter::mapPetToPetRs);

        log.info("foundAll {} pets", petFoundAllPaginRs.getSize());
        return new model.PetFindAllPaginRs(
                petFoundAllPaginRs.getContent(),
                petFoundAllPaginRs.getNumber(),
                petFoundAllPaginRs.getSize(),
                (int) petFoundAllPaginRs.getTotalElements(),
                petFoundAllPaginRs.getTotalPages(),
                petFoundAllPaginRs.hasNext()
        );
    }

    public model.PetRs findById(@NonNull UUID petId) {
        Pet rs = petCasheService.get(petId);

        if (Objects.nonNull(rs)) {
            log.info("found pet by {}", petId);
            return petConverter.mapPetToPetRs(rs);
        }

        rs = repository.findById(petId).orElseThrow(() -> {
            log.info("pet not found by {}", petId);
            return new PetNotFoundException(petId);
        });

        petCasheService.save(rs);
        return petConverter.mapPetToPetRs(rs);
    }
}
