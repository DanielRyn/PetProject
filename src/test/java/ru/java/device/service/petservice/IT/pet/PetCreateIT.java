package ru.java.device.service.petservice.IT.pet;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.ContentType;
import model.PageRq;
import model.PetFindAllRq;
import model.PetRq;
import model.PetRs;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import ru.java.device.service.petservice.IT.IntegrationTest;
import ru.java.device.service.petservice.entity.PetType;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PetCreateIT extends IntegrationTest {

    @Test
    public void create_ok() {
        PetFindAllRq rq = new PetFindAllRq();
        rq.setPageRq(new PageRq());

        ResponseEntity<model.PetFindAllPaginRs> found = restTemplate.postForEntity(
                "/rest/api/v1/pets",
                rq, model.PetFindAllPaginRs.class
        );
        assertEquals(HttpStatus.OK, found.getStatusCode());
        assertNotNull(found.getBody());
        assertTrue(found.getBody().getContent().isEmpty());
        assertEquals(0, found.getBody().getTotalCount());

        PetRq petRq = new PetRq("Tom", 12, PetType.BIRD.toString());
        String idempotentKey = UUID.randomUUID().toString();

        HttpEntity<Object> httpEntity = new HttpEntity<>(
                petRq,
                new HttpHeaders(MultiValueMap.fromSingleValue(Map.of(
                        "idempotentKey", idempotentKey,
                        HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()
                ))));
        ResponseEntity<model.PetRs> createdRs = restTemplate.exchange(
                "/rest/api/v1/pet",
                HttpMethod.POST,
                httpEntity,
                model.PetRs.class
        );
        assertEquals(HttpStatus.CREATED, createdRs.getStatusCode());

        PetRs createdBodyRs = createdRs.getBody();
        assertNotNull(createdBodyRs);
        assertNotNull(createdBodyRs.getId());
        assertNotNull(createdBodyRs.getName());
        assertNotNull(createdBodyRs.getAge());
        assertNotNull(createdBodyRs.getCreatedAt());
        assertNotNull(createdBodyRs.getType());

        assertEquals(petRq.getName(), createdBodyRs.getName());
        assertEquals(petRq.getAge(), createdBodyRs.getAge());
        assertEquals(petRq.getType(), createdBodyRs.getType());

        ResponseEntity<model.PetRs> foundCreatedRs = restTemplate.getForEntity(
                "/rest/api/v1/pets/" + createdRs.getBody().getId(), model.PetRs.class
        );
        assertEquals(HttpStatus.OK, foundCreatedRs.getStatusCode());

        PetRs gotBodyRs = foundCreatedRs.getBody();
        assertNotNull(gotBodyRs);
        assertEquals(createdBodyRs.getId(), gotBodyRs.getId());
        assertEquals(createdBodyRs.getName(), gotBodyRs.getName());
        assertEquals(createdBodyRs.getAge(), gotBodyRs.getAge());
        assertEquals(createdBodyRs.getType(), gotBodyRs.getType());
        assertEquals(createdBodyRs.getCreatedAt(), gotBodyRs.getCreatedAt());
    }
}
