package ru.java.device.service.petservice.service;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.java.device.service.petservice.entity.PetType;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Observed
public class PetTypeService {

    public model.PetTypesRs findTypes() {
        List<String> rs = Arrays.stream(PetType.values())
                .map(PetType::toString)
                .toList();

        log.info("get all pet types");
        return new model.PetTypesRs(rs);
    }
}
