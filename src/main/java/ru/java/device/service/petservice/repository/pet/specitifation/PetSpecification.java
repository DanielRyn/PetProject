package ru.java.device.service.petservice.repository.pet.specitifation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.java.device.service.petservice.entity.Pet;
import model.PetFindAllRq;
import model.PetFilterRq;
import ru.java.device.service.petservice.exception.badRequest.FilterKeyNotContainsIsValidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class PetSpecification implements Specification<Pet> {
    private final PetFindAllRq rq;

    @Override
    public @Nullable Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (Objects.isNull(rq) || CollectionUtils.isEmpty(rq.getFilter())) {
            return null;
        }

        List<Predicate> predicates = new ArrayList<>();

        for (model.PetFilterRq filter : rq.getFilter()) {
            validationFilterKey(filter);

            switch (filter.getKey().toLowerCase()) {
                //TODO вынести в entity
                case "name": {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get(filter.getKey())),
                                    "%" + filter.getValue().toLowerCase() + "%"
                            )
                    );
                    break;
                }
                //TODO вынести в entity
                case "age": {
                    predicates.add(
                            criteriaBuilder.equal(
                                    root.get("age"),
                                    filter.getValue()
                            )
                    );
                    break;
                }
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void validationFilterKey(PetFilterRq filterRq) {
        String key = filterRq.getKey();
        if (Objects.isNull(key) ||
                key.isBlank() ||
                !Arrays.stream(SpecificationKey.values())
                        .map(Enum::toString)
                        .toList()
                        .contains(filterRq.getKey().toUpperCase()))

            throw new FilterKeyNotContainsIsValidException(
                    filterRq.getKey(),
                    Arrays.stream(SpecificationKey.values())
                            .map(o -> o.toString().toLowerCase())
                            .toList()
            );
    }
}
