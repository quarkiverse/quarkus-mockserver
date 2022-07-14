package io.quarkiverse.mockserver.it.docker;

import static io.restassured.RestAssured.given;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.JsonBody;

import io.quarkiverse.mockserver.test.InjectMockServerClient;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;

@QuarkusTest
public class ExampleRestControllerTest extends AbstractTest {

    @InjectMockServerClient
    MockServerClient mockServerClient;

    @Test
    public void test200() {
        Map<String, Object> data = new HashMap<>(Map.of(
                "key-A", "value-A",
                "key-B", 1));

        // create mock rest endpoint
        mockServerClient.when(request().withPath("/example/1").withMethod("POST"))
                .respond(httpRequest -> response().withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(JsonBody.json(data)));

        ExampleRestController.Data request = new ExampleRestController.Data();
        request.key = "k1";
        request.value = "v1";

        JsonPath result = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .post("/test/1")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath();
        Assertions.assertNotNull(result);
        Assertions.assertEquals("value-A", result.getString("key-A"));
        Assertions.assertEquals(1, result.getInt("key-B"));
    }

    @Test
    public void test404() {
        ExampleRestController.Data request = new ExampleRestController.Data();
        request.key = "k1";
        request.value = "v1";
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .post("/test/2")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
