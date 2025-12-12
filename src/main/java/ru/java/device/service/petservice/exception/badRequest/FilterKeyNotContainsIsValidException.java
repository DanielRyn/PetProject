package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;

import java.util.List;

@Getter
public class FilterKeyNotContainsIsValidException extends ApplicationBadRequestException {

    public FilterKeyNotContainsIsValidException(String notValidKey, List<String> validkeys) {
        super("Filter has not valid key is key=\"" + notValidKey + "\". ValidKeys: " + validkeys);
    }
}
