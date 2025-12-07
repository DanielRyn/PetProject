package ru.java.device.service.petservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class OperationByIdempotemtKeyWasSuccessException extends RuntimeException {
    private final UUID idempotentKey;
    private final String message;
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public OperationByIdempotemtKeyWasSuccessException(UUID idempotentKey) {
        this.message = String.format("Operation by idempotemtKey {%s} was process", idempotentKey);
        this.idempotentKey = idempotentKey;
    }
}
