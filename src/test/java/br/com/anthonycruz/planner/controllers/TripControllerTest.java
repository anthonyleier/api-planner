package br.com.anthonycruz.planner.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import br.com.anthonycruz.planner.trip.TripRequest;
import br.com.anthonycruz.planner.trip.TripResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class TripControllerTest {
    private static TripResponse tripResponse;
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RestAssured.requestSpecification = specification;
    }

    @Test
    @Order(1)
    public void testCreate() {
        List<String> emails = new ArrayList<>();
        emails.add("joao.silva@gmail.com");
        emails.add("maria.souza@gmail.com");

        TripRequest request = new TripRequest(
                "Manaus/AM",
                "2024-12-15T09:00:00.000Z",
                "2024-12-20T18:00:00.000Z",
                emails,
                "carlos.lima@gmail.com",
                "Carlos Lima");

        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips")
                .body(request)
                .contentType("application/json")
                .when()
                .post();

        tripResponse = response.as(TripResponse.class);
        assertEquals(201, response.statusCode());
        assertNotNull(tripResponse.id());
    }

    @Test
    @Order(2)
    public void testGetTripDetails() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888/")
                .basePath("/trips/" + tripResponse.id())
                .when()
                .get();

        System.out.println(response.toString());

        assertEquals(tripResponse.id().toString(), response.jsonPath().getString("id"));
        assertEquals("Manaus/AM", response.jsonPath().getString("destination"));
        assertEquals("2024-12-15T09:00:00", response.jsonPath().getString("startsAt"));
        assertEquals("2024-12-20T18:00:00", response.jsonPath().getString("endsAt"));
        assertEquals("Carlos Lima", response.jsonPath().getString("ownerName"));
        assertEquals("carlos.lima@gmail.com", response.jsonPath().getString("ownerEmail"));
        assertFalse(response.jsonPath().getBoolean("confirmed"));
    }

    // updateTrip

    // confirmTrip

    // inviteParticipant

    // getAllParticipants

    // registerActivity

    // getAllActivities

    // registerLink

    // getAllLinks
}
