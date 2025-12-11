package ru.java.device.service.petservice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.converter.PetConverter;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.entity.cashe.PetCashe;
import ru.java.device.service.petservice.repository.pet.cashe.PetCasheRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetCasheService {
    private final PetCasheRepository repository;
    private final PetConverter converter;

    public Pet get(@NonNull UUID petId) {
        Optional<PetCashe> found = repository.findById(petId);
        if (found.isEmpty()) {
            return null;
        }

        log.info("found from cashe by key {}", petId);
        return converter.mapCasheToPet(found.get());
    }

    public void save(@NonNull Pet value) {
        repository.save(converter.mapToCashe(value));
        log.info("saved in cashe by key {}", value.getId());
    }

    public void delete(@NonNull UUID petId) {
        if (repository.existsById(petId)) {
            repository.deleteById(petId);
            log.info("deleted from cashe by key {}", petId);
        }
    }
}
