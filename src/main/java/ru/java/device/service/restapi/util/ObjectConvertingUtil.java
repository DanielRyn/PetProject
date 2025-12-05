package ru.java.device.service.restapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObjectConvertingUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String,Object> objToMap(Object obj, Map<String,Object> newFields){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(obj, Map.class);
        map.putAll(newFields);

        return map;
    }
}
