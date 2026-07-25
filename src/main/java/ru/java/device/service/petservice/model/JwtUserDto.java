package ru.java.device.service.petservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtUserDto {
    private Object userId;
    private Object userName;
    private List<Object> userRoles;
    private Object clientName;
}
