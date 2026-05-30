package ru.java.device.service.petservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.java.device.service.petservice.exception.badRequest.PetTypeNotValidException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@Slf4j
public enum PetType {

    CAT("Кошка"),
    BIRD("Птица"),
    FISH("Рыба"),
    REPTILE("Рептилия"),
    AMPHIBIAN("Амфибия"),
    INSECT("Насекомое"),
    ARACHNID("Паукообразное"),
    CRUSTACEAN("Ракообразное"),
    MOLLUSK("Моллюск"),
    ECHINODERM("Иглокожее"),
    DOG("Собака");

    private final String valueAsString;

    public static PetType get(String value) {
        return Arrays.stream(PetType.values())
                .filter(o -> o.toString().equals(value))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("pet type {} is not valid", value);
                    return new PetTypeNotValidException(
                            value,
                            Arrays.toString(PetType.values())
                    );
                });
    }
}
