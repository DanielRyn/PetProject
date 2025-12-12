package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;

@Getter
public class PetTypeNotValidException extends ApplicationBadRequestException {

    public PetTypeNotValidException(String petType) {
        super(String.format("pet type '%s' is not valid", petType));
    }
}
