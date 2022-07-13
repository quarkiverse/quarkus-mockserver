package io.quarkiverse.mockserver.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "quarkus-test", name = "mockserver", phase = ConfigPhase.RUN_TIME)
public class MockServerConfig {

    public static String ENDPOINT = "quarkus-test.mockserver.endpoint";

    public static String HOST = "quarkus-test.mockserver.host";

    public static String PORT = "quarkus-test.mockserver.port";

    public static String TEST_HOST = "quarkus-test.mockserver.client.host";

    public static String TEST_PORT = "quarkus-test.mockserver.client.port";

    /**
     * Host of the MockServer
     */
    @ConfigItem(name = "host")
    String host;

    /**
     * Port of the MockServer
     */
    @ConfigItem(name = "port")
    String port;

    /**
     * Endpoint of the MockServer
     */
    @ConfigItem(name = "endpoint")
    String endpoint;
}
