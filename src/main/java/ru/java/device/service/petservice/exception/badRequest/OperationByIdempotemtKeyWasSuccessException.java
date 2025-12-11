package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OperationByIdempotemtKeyWasSuccessException extends ApplicationBadRequestException {

    public OperationByIdempotemtKeyWasSuccessException(UUID idempotentKey) {
        super(String.format("Operation by idempotemtKey {%s} was process", idempotentKey));
    }
}
