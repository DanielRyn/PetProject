package ru.java.device.service.petservice.exception.badRequest;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationBadRequestException extends RuntimeException{
    private final String message;
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ApplicationBadRequestException(String message) {
        this.message = message;
    }
}
