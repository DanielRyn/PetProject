package ru.java.device.service.petservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Table(name = "pet")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    public Pet(String name, int age, PetType petType) {
        this.name = name;
        this.age = age;
        this.petType = petType;
        //todo возможно нужно вынести в date-util и покрыть тестом
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private PetType petType;
}
