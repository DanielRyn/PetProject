package ru.java.device.service.petservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.java.device.service.petservice.entity.cashe.IdempotentCashe;

import java.util.UUID;

@Repository
public interface idempotentRepository extends CrudRepository<IdempotentCashe, UUID> {

}
