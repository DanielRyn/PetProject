package ru.java.device.service.restapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.java.device.service.restapi.Exception.PetNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
@RequiredArgsConstructor
public class Advice {
    private final ObjectMapper mapper;

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handler(PetNotFoundException ex) {
        ErrorDto rs = ErrorDto.builder()
                .errorId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .message("Pet by %s not found".formatted(ex.getPetId()))
                .build();

        return new ResponseEntity<>(rs, ex.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Object> handler(MethodArgumentTypeMismatchException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .message(ex.getMessage())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .build();



        return ResponseEntity.badRequest().body(errorDto);
    }

    @Builder
    @Data
    public static class ErrorDto {
        private final UUID errorId;
        private final LocalDateTime createdAt;
        private final String message;
        private final Integer codeStatus;
    }
}
