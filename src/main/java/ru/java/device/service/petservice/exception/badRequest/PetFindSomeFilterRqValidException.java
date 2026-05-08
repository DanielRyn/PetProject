package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;

@Getter
public class PetFindSomeFilterRqValidException extends ApplicationBadRequestException {

    public PetFindSomeFilterRqValidException(String message) {
        super(message);
    }
}
