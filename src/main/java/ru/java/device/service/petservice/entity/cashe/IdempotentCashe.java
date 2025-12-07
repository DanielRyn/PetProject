package ru.java.device.service.petservice.entity.cashe;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RedisHash("idempotent")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdempotentCashe {
    @Id
    private UUID id;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
