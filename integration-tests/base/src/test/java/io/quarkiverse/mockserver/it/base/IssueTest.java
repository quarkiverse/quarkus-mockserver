package io.quarkiverse.mockserver.it.base;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.mock.Expectation;

import io.quarkiverse.mockserver.test.InjectMockServerClient;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Regression test for Issue #149: NoClassDefFoundError: Could not initialize
 * class com.github.fge.jackson.JacksonUtils
 */
@QuarkusTest
public class IssueTest extends AbstractTest {

        @InjectMockServerClient
        MockServerClient mockServerClient;

        @Test
        public void testUpsertDoesNotThrowNoClassDefFoundError() {
                mockServerClient.upsert(
                                new Expectation(
                                                request()
                                                                .withPath("/issue-149")
                                                                .withMethod("GET"))
                                                .thenRespond(
                                                                response()
                                                                                .withStatusCode(200)
                                                                                .withBody("Fixed!")));
        }
}
