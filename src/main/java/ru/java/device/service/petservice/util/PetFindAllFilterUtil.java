package ru.java.device.service.petservice.util;

import lombok.NonNull;
import model.PetFilterRq;
import ru.java.device.service.petservice.entity.PetType;
import ru.java.device.service.petservice.exception.badRequest.ApplicationBadRequestException;
import ru.java.device.service.petservice.exception.badRequest.PetFindSomeFilterRqValidException;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.java.device.service.petservice.repository.pet.specitifation.PetSpecification.SpecificationKey;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PetFindAllFilterUtil {

    public static void validationFilterKey(@NonNull List<model.PetFilterRq> filterRq){
        //Проверка чтобы key не повторялся больше одного раза
        //Группируем по key и ищем группу где больше 1 элемента, если находим - кидаем исключение
        Optional<PetFilterRq> found = filterRq.stream()
                .collect(Collectors.groupingBy(o -> o.getKey().toUpperCase()))
                .values().stream()
                .filter(o -> o.size() > 1)
                .map(o -> o.get(0))
                .findFirst();
        if (found.isPresent()) {
            throw new ApplicationBadRequestException(
                    String.format(
                            "Значение ключа key=\"%s\" не должно повторяться более одного раза",
                            found.get().getKey()
                    )
            );
        }

        filterRq.forEach(o -> {

            //Проверка что key валидно и соответствует SpecificationKey
            if (!Arrays.stream(SpecificationKey.values())
                    .map(Enum::toString)
                    .toList()
                    .contains(o.getKey().toUpperCase())) {

                throw new PetFindSomeFilterRqValidException(
                        String.format("Значение ключа фильтра имеет некоректное значение: \"%s\". Корректные значения: %s",
                                o.getKey(),
                                Arrays.stream(SpecificationKey.values())
                                        .map(o2 -> o2.toString().toUpperCase())
                                        .toList()
                        ));
            }

            if (SpecificationKey.AGE.toString().equals(o.getKey().toUpperCase())) {
                //Проверка на то что value является числом когда key=AGE
                if (!o.getValue().matches("-?\\d+")) {
                    throw new PetFindSomeFilterRqValidException(
                            String.format(
                                    "Value для key=\"%s\" должно являться числом",
                                    o.getKey()
                            )
                    );
                }

                int age = Integer.parseInt(o.getValue());
                if (age <= 0) {
                    throw new PetFindSomeFilterRqValidException(
                            String.format("\"%s\" должно быть больше нуля", o.getValue())
                    );
                }
            }

            if (SpecificationKey.TYPE.toString().equals(o.getKey().toUpperCase())) {
                //Проверка на то что если key=TYPE то value соответствует PetType
                //Если не соответствует то ru.java.device.service.petservice.entity.PetType.get кинет исключение
                PetType.get(o.getValue().toUpperCase());
            }
        });
    }

    public static PageRequest getPetFindAllRqToPageRequest(model.PetFindAllRq rq) {
        Sort sort = getSort(rq);
        if (Objects.isNull(rq) || Objects.isNull(rq.getPageRq())) {
            return PageRequest.of(
                    0,
                    10
            );
        }

        model.PageRq pageRq = rq.getPageRq();
        return PageRequest.of(
                Objects.isNull(pageRq.getPageNumber()) || pageRq.getPageNumber() < 0 ? 0 : pageRq.getPageNumber(),
                Objects.isNull(pageRq.getPageSize()) || pageRq.getPageSize() < 1 ? 10 : pageRq.getPageSize()
        ).withSort(sort);
    }

    //TODO Проверить чтобы поля сортировки всегда были разные
    private static Sort getSort(model.PetFindAllRq rq) {
        if (Objects.nonNull(rq) && Objects.nonNull(rq.getSort()) && !rq.getSort().isEmpty()) {
            List<Sort.Order> orders = rq.getSort().stream()
                    .map(o -> {
                        String direction = o.getType().toString();
                        String value = SpecificationKey.valueOf(o.getValue().toString()).getEntityValue();

                        return Sort.Direction.ASC.toString().equals(direction) ?
                                Sort.Order.asc(value) :
                                Sort.Order.desc(value);

                    }).toList();
            return Sort.by(orders);
        }

        return Sort.by(Sort.Direction.DESC, SpecificationKey.CREATED_AT.getEntityValue());
    }
}
