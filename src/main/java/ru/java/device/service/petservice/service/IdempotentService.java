package ru.java.device.service.petservice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.entity.cashe.IdempotentCashe;
import ru.java.device.service.petservice.repository.idempotentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotentService {
    private final idempotentRepository repository;

    public void add(@NonNull UUID key) {
        IdempotentCashe keyToSave = IdempotentCashe.builder()
                .id(key)
                .build();
        repository.save(keyToSave);
    }

    public boolean isExist(@NonNull UUID key) {
        return repository.existsById(key);
    }
}
