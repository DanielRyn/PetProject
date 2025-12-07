package ru.java.device.service.petservice.controller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.java.device.service.petservice.exception.badRequest.ApplicationBadRequestException;
import ru.java.device.service.petservice.exception.PetNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class Advice {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handler(PetNotFoundException ex) {
        ErrorDto rs = ErrorDto.builder()
                .errorId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .message("Pet not found by %s".formatted(ex.getPetId()))
                .build();

        return new ResponseEntity<>(rs, ex.getStatus());
    }

    @ExceptionHandler(exception = {ApplicationBadRequestException.class})
    public ResponseEntity<ErrorDto> badRequestHandler(Exception ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorFieldValidationDto> handler(MethodArgumentNotValidException ex) {

        List<ErrorFieldMessage> fieldsInfoError = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> ErrorFieldMessage.builder()
                        .fieldname(error.getField())
                        .errorMessage(error.getDefaultMessage())
                        .build()
                ).toList();

        ErrorFieldValidationDto rs = ErrorFieldValidationDto.builder()
                .errorDto(ErrorDto.builder()
                        .errorId(UUID.randomUUID())
                        .timestamp(LocalDateTime.now())
                        .message("fail validation request data")
                        .build())
                .errorFieldMessages(fieldsInfoError)
                .build();

        return ResponseEntity.badRequest().body(rs);
    }

    @Builder
    @Getter
    public static class ErrorDto {
        private final UUID errorId;
        private final LocalDateTime timestamp;
        private final String message;
    }

    @Builder
    @Getter
    public static class ErrorFieldValidationDto {
        private ErrorDto errorDto;
        List<ErrorFieldMessage> errorFieldMessages;
    }

    @Builder
    @Getter
    public static class ErrorFieldMessage {
        private String fieldname;
        private String errorMessage;
    }
}
