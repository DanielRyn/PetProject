package ru.java.device.service.petservice.IT;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.java.device.service.petservice.repository.pet.PetRepository;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    protected TestRestTemplate restTemplate;

    @Autowired
    protected PetRepository petRepository;

    @Container
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>()
            .withPassword("postgres")
            .withDatabaseName("postgres")
            .withUsername("postgres");

    @Container
    static final RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:7"))
            //TODO разобраться
            .withCommand(
                    "redis-server",
                    "--requirepass", "redis",
                    "--user", "redis", "on", ">redis", "allcommands", "allkeys"
            );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("POSTGRES_USERNAME", postgreSQLContainer::getUsername);
        registry.add("POSTGRES_PORT", () -> postgreSQLContainer.getMappedPort(postgreSQLContainer.getExposedPorts().get(0)));
        registry.add("POSTGRES_URL", postgreSQLContainer::getJdbcUrl);

        registry.add("REDIS_PORT", () -> redis.getMappedPort(redis.getExposedPorts().get(0)));
    }

    @BeforeEach
    public void setUp() {
        petRepository.deleteAll();
        restTemplate = new TestRestTemplate(
                new RestTemplateBuilder().rootUri("http://localhost:" + port)
        );
    }
}
