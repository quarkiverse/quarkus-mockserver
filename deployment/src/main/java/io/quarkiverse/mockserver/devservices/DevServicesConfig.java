package io.quarkiverse.mockserver.devservices;

import java.util.Optional;
import java.util.OptionalInt;

import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

public interface DevServicesConfig {

    /**
     * If DevServices has been explicitly enabled or disabled. DevServices is generally enabled
     * by default, unless there is an existing configuration present.
     * <p>
     * When DevServices is enabled Quarkus will attempt to automatically configure and start
     * a database when running in Dev or Test mode and when Docker is running.
     */
    @WithName("enabled")
    @WithDefault("true")
    boolean enabled();

    /**
     * Enabled or disable log of the mock-server
     */
    @WithName("log")
    @WithDefault("false")
    boolean log();

    /**
     * The container image name to use, for container based DevServices providers.
     */
    @WithName("image-name")
    Optional<String> imageName();

    /**
     * Optional fixed port the dev service will listen to.
     * <p>
     * If not defined, the port will be chosen randomly.
     */
    @WithName("port")
    OptionalInt port();

    /**
     * Indicates if the MockServer server managed by Quarkus Dev Services is shared.
     * When shared, Quarkus looks for running containers using label-based service discovery.
     * If a matching container is found, it is used, and so a second one is not started.
     * Otherwise, Dev Services for MockServer starts a new container.
     * <p>
     * The discovery uses the {@code quarkus-dev-service-mockserver} label.
     * The value is configured using the {@code service-name} property.
     * <p>
     * Container sharing is only used in dev mode.
     */
    @WithName("shared")
    @WithDefault("true")
    boolean shared();

    /**
     * The value of the {@code quarkus-dev-service-mockserver} label attached to the started container.
     * This property is used when {@code shared} is set to {@code true}.
     * In this case, before starting a container, Dev Services for Mockserver looks for a container with the
     * {@code quarkus-dev-service-mockserver} label
     * set to the configured value. If found, it will use this container instead of starting a new one. Otherwise, it
     * starts a new container with the {@code quarkus-dev-service-mockserver} label set to the specified value.
     * <p>
     * This property is used when you need multiple shared MockServer servers.
     */
    @WithName("service-name")
    @WithDefault("mock-server")
    String serviceName();

    /**
     * MockServer configuration file.
     */
    @WithName("config-file")
    Optional<String> configFile();

    /**
     * MockServer's configuration class-path binding. Useful for the test and CI builds.
     * When set to {@code true}, a test-container {@code withClasspathResourceMapping} method is used.
     */
    @WithName("config-class-path")
    @WithDefault("false")
    boolean configClassPath();

    /**
     * Helper to define the stop strategy for containers created by DevServices.
     * In particular, we don't want to actually stop the containers when they
     * have been flagged for reuse, and when the Testcontainers configuration
     * has been explicitly set to allow container reuse.
     * To enable reuse, ass {@literal testcontainers.reuse.enable=true} in your
     * {@literal .testcontainers.properties} file, to be stored in your home.
     *
     * @see <a href="https://www.testcontainers.org/features/configuration/">Testcontainers Configuration</a>.
     */
    @WithName("reuse")
    @WithDefault("false")
    boolean reuse();

    /**
     * The configuration directory to mount in the container /config.
     */
    @WithName("config-dir")
    Optional<String> configDir();

}
