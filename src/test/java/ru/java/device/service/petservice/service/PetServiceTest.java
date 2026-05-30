package ru.java.device.service.petservice.service;

import model.PetRq;
import model.PetRs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.java.device.service.petservice.converter.PetConverter;
import ru.java.device.service.petservice.entity.Pet;
import ru.java.device.service.petservice.entity.PetType;
import ru.java.device.service.petservice.repository.pet.PetRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @Mock
    private PetRepository repository;
    @Mock
    private PetConverter converter;
    @Mock
    private IdempotentService idempotentService;

    @InjectMocks
    private PetService petService;

    @Test
    void create_wnenCreateThatCreated_ok() {
        PetRq rq = new PetRq("Tom", 1, PetType.BIRD.toString());
        Pet saved = new Pet(
                UUID.randomUUID(),
                rq.getName(),
                rq.getAge(),
                PetType.BIRD,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
        PetRs rs = new PetRs(
                saved.getId(),
                saved.getName(),
                saved.getAge(),
                saved.getPetType().toString(),
                saved.getCreatedAt().toString()
        );
        UUID idempotentKey = UUID.randomUUID();

        when(idempotentService.isExist(any())).thenReturn(false);
        when(converter.mapPetRqToPet(any())).thenReturn(saved);
        doNothing().when(idempotentService).add(any());
        when(converter.mapPetToPetRs(any())).thenReturn(rs);
        when(repository.save(any())).thenReturn(saved);

        PetRs created = petService.create(rq, idempotentKey);

        assertEquals(rq.getName(), created.getName());
        assertEquals(rq.getAge(), created.getAge());
        assertEquals(rq.getType(), created.getType());
        assertEquals(saved.getId(), created.getId());
        assertEquals(saved.getCreatedAt().toString(), created.getCreatedAt());

        verify(idempotentService, times(1)).isExist(idempotentKey);
        verify(idempotentService, times(1)).isExist(any());

        verify(converter, times(1)).mapPetRqToPet(rq);
        verify(converter, times(1)).mapPetToPetRs(saved);

        verify(repository, times(1)).save(saved);

        verify(idempotentService, times(1)).add(idempotentKey);
        verify(idempotentService, times(1)).add(any());
    }
}
