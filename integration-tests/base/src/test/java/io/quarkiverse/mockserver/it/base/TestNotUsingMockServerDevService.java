package io.quarkiverse.mockserver.it.base;

import static io.restassured.RestAssured.given;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(TestNotUsingMockServerDevService.TestProfile.class)
public class TestNotUsingMockServerDevService {
    @Test
    public void test200() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/test/3")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.mockserver.devservices.enabled", "false");
        }
    }
}
