package io.quarkiverse.mockserver.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "mockserver", phase = ConfigPhase.RUN_TIME)
public class MockServerConfig {

    public static String ENDPOINT = "quarkus.mockserver.endpoint";

    public static String HOST = "quarkus.mockserver.host";

    public static String PORT = "quarkus.mockserver.port";

    public static String CLIENT_HOST = "quarkus.mockserver.client.host";

    public static String CLIENT_PORT = "quarkus.mockserver.client.port";

    /**
     * Host of the MockServer
     */
    @ConfigItem(name = "host", defaultValue = "localhost")
    String host;

    /**
     * Port of the MockServer
     */
    @ConfigItem(name = "port", defaultValue = "1080")
    String port;

    /**
     * Endpoint of the MockServer
     */
    @ConfigItem(name = "endpoint", defaultValue = "http://localhost:8080")
    String endpoint;

    /**
     * Host of the MockServer for the MockServerClient
     */
    @ConfigItem(name = "client.host", defaultValue = "localhost")
    String clientHost;

    /**
     * Port of the MockServer for the MockServerClient
     */
    @ConfigItem(name = "client.port", defaultValue = "1080")
    String clientPort;
}
