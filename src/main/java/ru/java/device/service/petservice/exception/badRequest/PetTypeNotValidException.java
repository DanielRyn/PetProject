package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;

@Getter
public class PetTypeNotValidException extends ApplicationBadRequestException {

    public PetTypeNotValidException(String petType, String validValues) {
        super(String.format(
                "Тип питомца \"%s\" не является корректным, корректные значения: \"%s\"",
                petType,
                validValues
        ));
    }
}
