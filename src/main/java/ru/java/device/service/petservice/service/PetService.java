package ru.java.device.service.petservice.service;

import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
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
@Observed
public class PetService {
    private final PetConverter petConverter;
    private final PetCasheService petCasheService;
    private final IdempotentService idempotentService;
    private final PetRepository petRepository;

    public PetRs create(@NonNull PetRq rq, @NonNull UUID idempotentKey) {
        if (idempotentService.isExist(idempotentKey)) {
            throw new OperationByIdempotemtKeyWasSuccessException(idempotentKey);
        }

        Pet petToSave = petConverter.mapPetRqToPet(rq);
        Pet saved = petRepository.save(petToSave);
        idempotentService.add(idempotentKey);

        log.info("pet was save {}", saved.getId());
        return petConverter.mapPetToPetRs(saved);
    }

    public PetsRs create(@NonNull CreatePetsRq rq, @NonNull UUID idempotentKey) {
        if (idempotentService.isExist(idempotentKey)) {
            throw new OperationByIdempotemtKeyWasSuccessException(idempotentKey);
        }

        List<PetRs> petRs = new ArrayList<>();
        for (PetRq petRq : rq.getPetsRq()) {

            Pet petToSave = petConverter.mapPetRqToPet(petRq);
            Pet saved = petRepository.save(petToSave);
            petRs.add(petConverter.mapPetToPetRs(saved));
        }

        idempotentService.add(idempotentKey);

        log.info("was save {} pets", petRs.size());
        return new PetsRs(petRs);
    }

    @Transactional
    public void delete(@NonNull UUID petId) {
        if (!petRepository.existsPetByIsDeletedAndById(petId)) {
            log.info("pet not found by {}", petId);
            throw new PetNotFoundException(petId);
        }

        //TODO LocalDateTime.now() --> Нужно по определённой таймзоне брать текущ. дату
        petRepository.setDeletedStatusById(petId, LocalDateTime.now());
        petCasheService.delete(petId);
        log.info("pet by {} success deleted", petId);
    }

    @Transactional
    public PetsDeleteRs delete(@NonNull PetsDeleteRq rq) {
        List<UUID> successDeleted = new ArrayList<>();
        List<PetsNotDeleted> failedDeleted = new ArrayList<>();

        List<UUID> distinctPetIds = rq.getId().stream().distinct().toList();
        for (UUID petId : distinctPetIds) {
            try {
                delete(petId);
                successDeleted.add(petId);
            } catch (Exception e) {
                failedDeleted.add(new PetsNotDeleted(
                        petId,
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
        Page<PetRs> petFoundAllPaginRs = petRepository
                .findAll(
                        new PetSpecification(rq),
                        PetFindAllFilterUtil.getPetFindAllRqToPageRequest(rq)
                ).map(petConverter::mapPetToPetRs);

        //TODO Разобраться с логами
        log.info("foundAll {} pets, rq={}", petFoundAllPaginRs.getSize(), rq);
        return new PetFindAllPaginRs(
                petFoundAllPaginRs.getContent(),
                petFoundAllPaginRs.getNumber(),
                petFoundAllPaginRs.getSize(),
                (int) petFoundAllPaginRs.getTotalElements(),
                petFoundAllPaginRs.getTotalPages(),
                petFoundAllPaginRs.hasNext()
        );
    }

    public PetRs findById(@NonNull UUID petId) {
        Pet rs = petCasheService.get(petId);

        if (Objects.nonNull(rs)) {
            return petConverter.mapPetToPetRs(rs);
        }

        rs = petRepository.findById(petId).orElseThrow(() -> {
            log.info("pet not found by {}", petId);
            return new PetNotFoundException(petId);
        });

        log.info("found pet by {}", petId);
        petCasheService.save(rs);
        return petConverter.mapPetToPetRs(rs);
    }
}
