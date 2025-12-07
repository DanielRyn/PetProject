package ru.java.device.service.petservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PetNotFoundException extends RuntimeException {
    private UUID petId;
    private final HttpStatus status = HttpStatus.NOT_FOUND;
}
