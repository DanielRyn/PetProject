package ru.java.device.service.petservice.util;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private final static String FORMATTED_PATTERH = "yyyy-MM-dd HH:mm:ss";

    public static String formattedDate(@NonNull LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTED_PATTERH);
        return localDateTime.format(formatter);
    }
}
