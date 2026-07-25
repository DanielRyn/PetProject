package ru.java.device.service.petservice.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.java.device.service.petservice.exception.badRequest.PetTypeNotValidException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetTypeTest {

    @Test
    void get_whenGetThenGot_ok() {
        assertEquals(PetType.CAT, PetType.get(PetType.CAT.toString()));
    }

    @Test
    void get_whenGetThenThrowsException_error() {
        assertThrows(PetTypeNotValidException.class, () -> PetType.get("not valid rq"));
    }
}
