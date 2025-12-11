package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class FilterKeyNotContainsIsValidException extends ApplicationBadRequestException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public FilterKeyNotContainsIsValidException(String notValidKey, List<String> validkeys) {
        super("Filter has not valid key is key=\"" + notValidKey + "\". ValidKeys: " + validkeys);
    }
}
