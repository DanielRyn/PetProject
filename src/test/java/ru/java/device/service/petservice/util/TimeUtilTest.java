package ru.java.device.service.petservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeUtilTest {
    private static final String MATCHER_PATTERN = "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$";

    @Test
    void formattedDate_ok() {
        LocalDateTime rq = LocalDateTime.now();
        String rs = TimeUtil.formattedDate(rq);
        assertTrue(rs.matches(MATCHER_PATTERN));
    }

    @Test
    void formattedDate_whenRqIsNull_thenThrowException_error() {
        assertThrows(NullPointerException.class, () -> TimeUtil.formattedDate(null));
    }
}
