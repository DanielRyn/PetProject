package ru.java.device.service.restapi.converter;

import model.PageRq;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.java.device.service.restapi.entity.Pet;
import model.PetRs;
import model.PetFindAllRq;

import java.util.Objects;

@Service
public class PetConverter {

    public PetRs petToPetRs(Pet pet) {
        PetRs rs = new PetRs();

        if (Objects.nonNull(pet)) {
            return new PetRs(
                    pet.getId(),
                    pet.getName(),
                    pet.getAge(),
                    pet.getCreatedAt().toString()
            );
        }
        return rs;
    }

    public PageRequest getPetFindAllRqToPageRequest(PetFindAllRq rq) {
        if (Objects.isNull(rq) || Objects.isNull(rq.getPageRq())) {
            return PageRequest.of(0, 10);
        }

        PageRq pageRq = rq.getPageRq();
        return PageRequest.of(
                Objects.isNull(pageRq.getPageNumber()) || pageRq.getPageNumber() < 0 ? 0 : pageRq.getPageNumber(),
                Objects.isNull(pageRq.getPageSize()) || pageRq.getPageSize() < 1 ? 10 : pageRq.getPageSize()
        );
    }
}
