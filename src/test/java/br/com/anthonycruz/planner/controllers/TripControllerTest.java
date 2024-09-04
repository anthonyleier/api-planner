package br.com.anthonycruz.planner.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import br.com.anthonycruz.planner.dtos.ActivityDTO;
import br.com.anthonycruz.planner.dtos.LinkDTO;
import br.com.anthonycruz.planner.dtos.ParticipantDTO;
import br.com.anthonycruz.planner.dtos.TripDTO;
import br.com.anthonycruz.planner.requests.ActivityRequest;
import br.com.anthonycruz.planner.requests.LinkRequest;
import br.com.anthonycruz.planner.requests.ParticipantRequest;
import br.com.anthonycruz.planner.requests.TripRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class TripControllerTest {
    private static UUID tripID;
    private static UUID participantID;
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

        TripDTO tripDTO = response.as(TripDTO.class);
        tripID = tripDTO.id();

        assertEquals(201, response.statusCode());
        assertNotNull(tripDTO.id());
        assertEquals("Manaus/AM", tripDTO.destination());
        assertEquals("2024-12-15T09:00", tripDTO.startsAt().toString());
        assertEquals("2024-12-20T18:00", tripDTO.endsAt().toString());
        assertEquals("Carlos Lima", tripDTO.ownerName());
        assertEquals("carlos.lima@gmail.com", tripDTO.ownerEmail());
        assertFalse(tripDTO.isConfirmed());
    }

    @Test
    @Order(2)
    public void testGetTripDetails() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888/")
                .basePath("/trips/" + tripID)
                .when()
                .get();
        TripDTO tripDTO = response.as(TripDTO.class);

        assertEquals(200, response.statusCode());
        assertEquals(tripID, tripDTO.id());
        assertEquals("Manaus/AM", tripDTO.destination());
        assertEquals("2024-12-15T09:00", tripDTO.startsAt().toString());
        assertEquals("2024-12-20T18:00", tripDTO.endsAt().toString());
        assertEquals("Carlos Lima", tripDTO.ownerName());
        assertEquals("carlos.lima@gmail.com", tripDTO.ownerEmail());
        assertFalse(tripDTO.isConfirmed());
    }

    @Test
    @Order(3)
    public void testUpdate() {
        TripRequest request = new TripRequest(
                "Florianópolis/SC",
                "2025-12-15T09:00:00.000Z",
                "2025-12-20T18:00:00.000Z",
                null,
                null,
                null);

        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/" + tripID)
                .body(request)
                .contentType("application/json")
                .when()
                .put();
        TripDTO tripDTO = response.as(TripDTO.class);

        assertEquals(200, response.statusCode());
        assertEquals(tripID, tripDTO.id());
        assertEquals("Florianópolis/SC", tripDTO.destination());
        assertEquals("2025-12-15T09:00", tripDTO.startsAt().toString());
        assertEquals("2025-12-20T18:00", tripDTO.endsAt().toString());
        assertEquals("Carlos Lima", tripDTO.ownerName());
        assertEquals("carlos.lima@gmail.com", tripDTO.ownerEmail());
        assertFalse(tripDTO.isConfirmed());
    }

    @Test
    @Order(4)
    public void testConfirm() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888/")
                .basePath("/trips/" + tripID + "/confirm")
                .when()
                .get();
        TripDTO tripDTO = response.as(TripDTO.class);

        assertEquals(200, response.statusCode());
        assertEquals(tripID, tripDTO.id());
        assertEquals("Florianópolis/SC", tripDTO.destination());
        assertEquals("2025-12-15T09:00", tripDTO.startsAt().toString());
        assertEquals("2025-12-20T18:00", tripDTO.endsAt().toString());
        assertEquals("Carlos Lima", tripDTO.ownerName());
        assertEquals("carlos.lima@gmail.com", tripDTO.ownerEmail());
        assertTrue(tripDTO.isConfirmed());
    }

    @Test
    @Order(5)
    public void testInviteParticipant() {
        ParticipantRequest request = new ParticipantRequest("", "jose.silva123@gmail.com");

        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/" + tripID + "/invite")
                .body(request)
                .contentType("application/json")
                .when()
                .post();
        ParticipantDTO participantDTO = response.as(ParticipantDTO.class);
        participantID = participantDTO.id();

        assertEquals(200, response.statusCode());
        assertNotNull(participantDTO.id());
        assertEquals("", participantDTO.name());
        assertEquals("jose.silva123@gmail.com", participantDTO.email());
        assertFalse(participantDTO.isConfirmed());
    }

    @Test
    @Order(6)
    public void testGetAllParticipants() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888/")
                .basePath("/trips/" + tripID + "/participants")
                .when()
                .get();

        List<ParticipantDTO> participants = response.as(new TypeRef<List<ParticipantDTO>>() {
        });
        ParticipantDTO participantDTO = participants.getFirst();

        assertEquals(200, response.statusCode());
        assertNotNull(participantDTO.id());
        assertEquals("", participantDTO.name());
        assertEquals("jose.silva123@gmail.com", participantDTO.email());
        assertFalse(participantDTO.isConfirmed());
    }

    @Test
    @Order(7)
    public void testRegisterActivity() {
        ActivityRequest request = new ActivityRequest("Visit Canasvieiras Beach", "2025-12-17T08:00:00.000Z");

        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/" + tripID + "/activities")
                .body(request)
                .contentType("application/json")
                .when()
                .post();
        ActivityDTO activityDTO = response.as(ActivityDTO.class);

        assertEquals(200, response.statusCode());
        assertNotNull(activityDTO.id());
        assertEquals("Visit Canasvieiras Beach", activityDTO.title());
        assertEquals("2025-12-17T08:00", activityDTO.occursAt().toString());
    }

    @Test
    @Order(8)
    public void testGetAllActivities() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888/")
                .basePath("/trips/" + tripID + "/activities")
                .when()
                .get();

        List<ActivityDTO> activities = response.as(new TypeRef<List<ActivityDTO>>() {
        });
        ActivityDTO activityDTO = activities.getFirst();

        assertEquals(200, response.statusCode());
        assertNotNull(activityDTO.id());
        assertEquals("Visit Canasvieiras Beach", activityDTO.title());
        assertEquals("2025-12-17T08:00", activityDTO.occursAt().toString());
    }

    @Test
    @Order(9)
    public void testRegisterLink() {
        LinkRequest request = new LinkRequest("Booking", "https://booking.com");

        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/" + tripID + "/links")
                .body(request)
                .contentType("application/json")
                .when()
                .post();
        LinkDTO linkDTO = response.as(LinkDTO.class);

        assertEquals(200, response.statusCode());
        assertNotNull(linkDTO.id());
        assertEquals("Booking", linkDTO.title());
        assertEquals("https://booking.com", linkDTO.url());
    }

    @Test
    @Order(10)
    public void testGetAllLinks() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888/")
                .basePath("/trips/" + tripID + "/links")
                .when()
                .get();

        List<LinkDTO> links = response.as(new TypeRef<List<LinkDTO>>() {
        });
        LinkDTO linkDTO = links.getFirst();

        assertEquals(200, response.statusCode());
        assertNotNull(linkDTO.id());
        assertEquals("Booking", linkDTO.title());
        assertEquals("https://booking.com", linkDTO.url());
    }

    @Test
    @Order(11)
    public void testConfirmParticipant() {
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/participants/" + participantID + "/confirm")
                .when()
                .get();
        ParticipantDTO participantDTO = response.as(ParticipantDTO.class);

        assertEquals(200, response.statusCode());
        assertNotNull(participantDTO.id());
        assertEquals("", participantDTO.name());
        assertEquals("jose.silva123@gmail.com", participantDTO.email());
        assertTrue(participantDTO.isConfirmed());
    }

    @Test
    @Order(12)
    public void testUpdateParticipant() {
        ParticipantRequest request = new ParticipantRequest("José da Silva", "jose.silva456@gmail.com");
        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/participants/" + participantID)
                .body(request)
                .contentType("application/json")
                .when()
                .put();
        ParticipantDTO participantDTO = response.as(ParticipantDTO.class);

        assertEquals(200, response.statusCode());
        assertNotNull(participantDTO.id());
        assertEquals("José da Silva", participantDTO.name());
        assertEquals("jose.silva456@gmail.com", participantDTO.email());
        assertTrue(participantDTO.isConfirmed());
    }

    @Test
    @Order(13)
    public void testUploadPhoto() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "some image".getBytes());
        RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/{id}/photos")
                .contentType(ContentType.MULTIPART)
                .multiPart("file", file.getOriginalFilename(), file.getBytes())
                .pathParam("id", tripID)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("filename", Matchers.equalTo("test.jpg"));
    }

    @Test
    @Order(14)
    void testDownloadPhoto() {
        String filename = "test.jpg";
        RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/{id}/photos/{filename}")
                .pathParam("id", tripID)
                .pathParam("filename", filename)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("image/jpeg")
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

    @Test
    @Order(15)
    void testGetAllPhotos() {
        RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/{id}/photos")
                .pathParam("id", tripID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", Matchers.greaterThanOrEqualTo(1));
    }

    @Test
    @Order(16)
    void testDeletePhoto() {
        String filename = "test.jpg";
        RestAssured
                .given()
                .baseUri("http://localhost:8888")
                .basePath("/trips/{id}/photos/{filename}")
                .pathParam("id", tripID)
                .pathParam("filename", filename)
                .when()
                .delete()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
