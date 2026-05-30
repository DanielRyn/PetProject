package ru.java.device.service.petservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Table(name = "pet")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pet {

    public Pet(String name, int age, PetType petType) {
        this.name = name;
        this.age = age;
        this.petType = petType;
        this.createdAt = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        this.isDeleted = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private PetType petType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
