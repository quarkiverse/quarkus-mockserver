package io.quarkiverse.mockserver.it.base;

import io.quarkiverse.mockserver.test.MockServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;

@QuarkusTestResource(MockServerTestResource.class)
public class AbstractTest {

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new ResponseLoggingFilter());
    }

}
