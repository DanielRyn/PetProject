package ru.java.device.service.petservice.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.java.device.service.petservice.entity.Pet;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID>, JpaSpecificationExecutor<Pet> {

    @Query("select count(*) > 0 from Pet p where p.isDeleted = false and p.id = :petId")
    boolean existsPetByIsDeletedAndById(@Param("petId") UUID petId);

    @Modifying
    @Query("update Pet p set p.isDeleted = true, p.deletedAt = :petDeletedAt where p.id = :petId")
    void setDeletedStatusById(
            @Param("petId") UUID petId,
            @Param("petDeletedAt") LocalDateTime petDeletedAt
    );
}
