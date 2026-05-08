package ru.java.device.service.petservice.repository.pet.specitifation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.java.device.service.petservice.util.PetFindAllFilterUtil;
import ru.java.device.service.petservice.entity.Pet;

import java.util.*;

@RequiredArgsConstructor
public class PetSpecification implements Specification<Pet> {
    private final model.PetFindAllRq rq;

    @Override
    public @Nullable Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (Objects.isNull(rq) || CollectionUtils.isEmpty(rq.getFilter())) {
            return null;
        }

        List<Predicate> predicates = new ArrayList<>();
        PetFindAllFilterUtil.validationFilterKey(rq.getFilter());

        var filterRqs = rq.getFilter().stream().filter(Objects::nonNull).toList();
        for (model.PetFilterRq filter : filterRqs) {

            if (SpecificationKey.NAME.toString().equals(filter.getKey().toUpperCase())) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(SpecificationKey.NAME.getEntityValue())),
                                "%" + filter.getValue().toLowerCase() + "%"
                        )
                );
            }

            if (SpecificationKey.AGE.toString().equals(filter.getKey().toUpperCase())) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(SpecificationKey.AGE.getEntityValue()),
                                filter.getValue()
                        )
                );
            }

            if (SpecificationKey.TYPE.toString().equals(filter.getKey().toUpperCase())) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(SpecificationKey.TYPE.getEntityValue()),
                                filter.getValue().toUpperCase()
                        )
                );
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @AllArgsConstructor
    @Getter
    //TODO Подумать над тем чтобы сделать DenormolizeService и вынести эту логику туда
    public enum SpecificationKey {
        NAME("name"),
        AGE("age"),
        TYPE("petType"),
        CREATED_AT("createdAt");

        private final String entityValue;
    }
}
