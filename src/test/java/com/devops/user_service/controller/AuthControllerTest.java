package com.devops.user_service.controller;

import com.devops.user_service.repository.UserRepository;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    final static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:18.0.0";

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Container
    static KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
            .withRealmImportFile("data/import/realm.json");

    @DynamicPropertySource
    static void registerKeycloakProperties(DynamicPropertyRegistry registry) {
        String authServerUrl = keycloak.getAuthServerUrl();
        String issueruri = authServerUrl + "realms/devops";
        registry.add("keycloak.auth-server-url", () -> authServerUrl);
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () -> issueruri);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> issueruri);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        keycloak.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        keycloak.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        String body = getBody();
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/auth/registration")
                .then()
                .statusCode(201);

        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    private String getBody() {
        return """
                {
                    "firstname": "as",
                    "lastname": "Hoastic",
                    "username": "testtttttt",
                    "email": "testaaaaares@gostic.com",
                    "password": "host123",
                    "role": "ROLE_GUEST",
                    "address": {
                        "street": "Ulica 1",
                        "city": "Novi Sad",
                        "country": "Serbia"
                    }
                }
                """;
    }
}
