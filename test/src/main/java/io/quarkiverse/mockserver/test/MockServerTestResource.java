package io.quarkiverse.mockserver.test;

import java.util.Map;

import org.mockserver.client.MockServerClient;

import io.quarkiverse.mockserver.runtime.MockServerConfig;
import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class MockServerTestResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    static MockServerClient CLIENT;

    @Override
    public Map<String, String> start() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(CLIENT,
                new TestInjector.AnnotatedAndMatchesType(InjectMockServerClient.class, MockServerClient.class));
    }

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        String host = context.devServicesProperties().get(MockServerConfig.TEST_HOST);
        String port = context.devServicesProperties().get(MockServerConfig.TEST_PORT);
        CLIENT = new MockServerClient(host, Integer.parseInt(port));
    }
}
