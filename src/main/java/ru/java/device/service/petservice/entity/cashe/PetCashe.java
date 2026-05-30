package ru.java.device.service.petservice.entity.cashe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.entity.PetType;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RedisHash("pet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetCashe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private int age;
    private PetType type;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;
}
