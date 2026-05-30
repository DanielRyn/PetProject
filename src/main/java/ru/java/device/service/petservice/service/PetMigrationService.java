package ru.java.device.service.petservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.entity.PetType;
import ru.java.device.service.petservice.repository.pet.PetRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Random;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class PetMigrationService {
    private final Faker faker = new Faker();
    private final PetRepository repository;

    @Scheduled(fixedRate = 1_000)
    public void generate() {

        int randomIndex = new Random().nextInt(0, PetType.values().length - 1);
        LocalDateTime createdDate = LocalDateTime.of(
                faker.timeAndDate().birthday(),
                LocalTime.now(ZoneId.of("Europe/Moscow"))
        );

        Pet petTOSave = Pet.builder()
                .name(faker.name().firstName())
                .age(new Random().nextInt(1, 65))
                .petType(PetType.values()[randomIndex])
                .createdAt(createdDate)
                .isDeleted(false)
                .build();

        repository.save(petTOSave);
        log.info("success generated {}", petTOSave.getId());
    }
}
