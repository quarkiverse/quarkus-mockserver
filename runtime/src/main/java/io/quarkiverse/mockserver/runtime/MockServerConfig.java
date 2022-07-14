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

    /**
     * Host of the MockServer for the MockServerClient
     */
    @ConfigItem(name = "client.host")
    String clientHost;

    /**
     * Port of the MockServer for the MockServerClient
     */
    @ConfigItem(name = "client.port")
    String clientPort;
}
