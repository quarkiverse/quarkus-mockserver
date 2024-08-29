package io.quarkiverse.mockserver.runtime;

import io.quarkus.runtime.annotations.ConfigDocFilename;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigDocFilename("quarkus-mockserver.adoc")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.mockserver")
public interface MockServerConfig {

    String ENDPOINT = "quarkus.mockserver.endpoint";

    String HOST = "quarkus.mockserver.host";

    String PORT = "quarkus.mockserver.port";

    String CLIENT_HOST = "quarkus.mockserver.client.host";

    String CLIENT_PORT = "quarkus.mockserver.client.port";

    /**
     * Host of the MockServer
     */
    @WithName("host")
    @WithDefault("localhost")
    String host();

    /**
     * Port of the MockServer
     */
    @WithName("port")
    @WithDefault("1080")
    String port();

    /**
     * Endpoint of the MockServer
     */
    @WithName("endpoint")
    @WithDefault("http://localhost:8080")
    String endpoint();

    /**
     * Host of the MockServer for the MockServerClient
     */
    @WithName("client.host")
    @WithDefault("localhost")
    String clientHost();

    /**
     * Port of the MockServer for the MockServerClient
     */
    @WithName("client.port")
    @WithDefault("1080")
    String clientPort();
}
