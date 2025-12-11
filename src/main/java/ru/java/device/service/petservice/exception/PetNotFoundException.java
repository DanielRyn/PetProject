package ru.java.device.service.petservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class PetNotFoundException extends RuntimeException {
    private final UUID petId;
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public PetNotFoundException(UUID petId) {
        super("Pet not found by %s".formatted(petId));
        this.petId = petId;
    }
}
