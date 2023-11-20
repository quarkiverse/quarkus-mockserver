package io.quarkiverse.mockserver.devservices;

import static io.quarkus.runtime.LaunchMode.DEVELOPMENT;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

import org.jboss.logging.Logger;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import io.quarkiverse.mockserver.runtime.MockServerConfig;
import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CuratedApplicationShutdownBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.DevServicesSharedNetworkBuildItem;
import io.quarkus.deployment.builditem.DockerStatusBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.console.ConsoleInstalledBuildItem;
import io.quarkus.deployment.console.StartupLogCompressor;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.deployment.logging.LoggingSetupBuildItem;
import io.quarkus.devservices.common.ConfigureUtil;
import io.quarkus.devservices.common.ContainerLocator;
import io.quarkus.runtime.LaunchMode;

public class DevServicesMockServerProcessor {

    private static final Logger log = Logger.getLogger(DevServicesMockServerProcessor.class);

    /**
     * Label to add to shared Dev Service for MockServer running in containers.
     * This allows other applications to discover the running service and use it instead of starting a new instance.
     */
    private static final String DEV_SERVICE_LABEL = "quarkus-dev-service-mockserver";
    private static final int MOCKSERVER_EXPOSED_PORT = MockServerContainer.PORT;

    private static final ContainerLocator containerLocator = new ContainerLocator(DEV_SERVICE_LABEL, MOCKSERVER_EXPOSED_PORT);

    private static final String FEATURE_NAME = "mock-server";

    private static final String DEFAULT_IMAGE = "mockserver/mockserver:5.15.0";

    private static volatile DevServicesResultBuildItem.RunningDevService devServices;
    private static volatile MockServerBuildTimeConfig.DevServiceConfiguration capturedDevServicesConfiguration;
    private static volatile boolean first = true;

    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = { GlobalDevServicesConfig.Enabled.class })
    public DevServicesResultBuildItem startMockServerContainers(LaunchModeBuildItem launchMode,
            DockerStatusBuildItem dockerStatusBuildItem,
            List<DevServicesSharedNetworkBuildItem> devServicesSharedNetworkBuildItem,
            MockServerBuildTimeConfig config,
            Optional<ConsoleInstalledBuildItem> consoleInstalledBuildItem,
            CuratedApplicationShutdownBuildItem closeBuildItem,
            LoggingSetupBuildItem loggingSetupBuildItem,
            GlobalDevServicesConfig devServicesConfig) {

        MockServerBuildTimeConfig.DevServiceConfiguration currentDevServicesConfiguration = config.defaultDevService;

        // figure out if we need to shut down and restart existing MockServer containers
        // if not and the MockServer containers have already started we just return
        if (devServices != null) {
            boolean restartRequired = !currentDevServicesConfiguration.equals(capturedDevServicesConfiguration);
            if (!restartRequired) {
                return devServices.toBuildItem();
            }
            try {
                devServices.close();
            } catch (Throwable e) {
                log.error("Failed to stop MockServer container", e);
            }
            devServices = null;
            capturedDevServicesConfiguration = null;
        }

        capturedDevServicesConfiguration = currentDevServicesConfiguration;

        StartupLogCompressor compressor = new StartupLogCompressor(
                (launchMode.isTest() ? "(test) " : "") + "MockServer Dev Services Starting:", consoleInstalledBuildItem,
                loggingSetupBuildItem);
        try {

            devServices = startContainer(dockerStatusBuildItem,
                    currentDevServicesConfiguration.devservices,
                    launchMode.getLaunchMode(),
                    !devServicesSharedNetworkBuildItem.isEmpty(), devServicesConfig.timeout);
            if (devServices == null) {
                compressor.closeAndDumpCaptured();
            } else {
                compressor.close();
            }

        } catch (Throwable t) {
            compressor.closeAndDumpCaptured();
            throw new RuntimeException(t);
        }

        if (devServices == null) {
            return null;
        }

        if (first) {
            first = false;
            Runnable closeTask = () -> {
                if (devServices != null) {
                    try {
                        devServices.close();
                    } catch (Throwable t) {
                        log.error("Failed to stop MockServer", t);
                    }
                }
                first = true;
                devServices = null;
                capturedDevServicesConfiguration = null;
            };
            closeBuildItem.addCloseTask(closeTask, true);
        }
        if (devServices.isOwner()) {
            log.infof("The mock-server server is ready to accept connections on http://%s:%s",
                    devServices.getConfig().get(MockServerConfig.CLIENT_HOST),
                    devServices.getConfig().get(MockServerConfig.CLIENT_PORT));
            log.infof("The mock-server dashboard http://%s:%s/mockserver/dashboard",
                    devServices.getConfig().get(MockServerConfig.CLIENT_HOST),
                    devServices.getConfig().get(MockServerConfig.CLIENT_PORT));
        }
        return devServices.toBuildItem();
    }

    private DevServicesResultBuildItem.RunningDevService startContainer(DockerStatusBuildItem dockerStatusBuildItem,
            DevServicesConfig devServicesConfig, LaunchMode launchMode,
            boolean useSharedNetwork, Optional<Duration> timeout) {
        if (!devServicesConfig.enabled) {
            // explicitly disabled
            log.debug("Not starting devservices for rest client as it has been disabled in the config");
            return null;
        }

        if (!dockerStatusBuildItem.isDockerAvailable()) {
            log.warnf("Please configure '%s' or get a working MockServer instance", MockServerConfig.ENDPOINT);
            return null;
        }

        DockerImageName dockerImageName = DockerImageName.parse(devServicesConfig.imageName.orElse(DEFAULT_IMAGE))
                .asCompatibleSubstituteFor("mockserver/mockserver");

        Supplier<DevServicesResultBuildItem.RunningDevService> defaultMockServerSupplier = () -> {
            QuarkusPortMockServerContainer container = new QuarkusPortMockServerContainer(dockerImageName,
                    devServicesConfig.port,
                    launchMode == DEVELOPMENT ? devServicesConfig.serviceName : null, useSharedNetwork);
            timeout.ifPresent(container::withStartupTimeout);

            // enabled or disable container logs
            if (devServicesConfig.log) {
                container.withLogConsumer(ContainerLogger.create(devServicesConfig.serviceName));
            }

            // Add mockserver configuration properties file
            if (devServicesConfig.configFile.isPresent()) {
                String configFile = devServicesConfig.configFile.get();
                if (devServicesConfig.configClassPath) {
                    container.withClasspathResourceMapping(configFile, "/config/mockserver.properties", BindMode.READ_ONLY);
                    log.infof(
                            "MockServer configuration class-path file '%s' mount to '/config/mockserver.properties' container file.",
                            configFile);
                } else {
                    if (Files.isRegularFile(Path.of(configFile))) {
                        container.withFileSystemBind(configFile, "/config/mockserver.properties", BindMode.READ_ONLY);
                        log.infof(
                                "MockServer configuration local file '%s' mount to '/config/mockserver.properties' container file.",
                                configFile);
                    } else {
                        log.warnf("MockServer configuration local file '%s' is regular file.", configFile);
                    }
                }
            }

            // mount directory with mocks
            if (devServicesConfig.configDir.isPresent()) {
                String configDir = devServicesConfig.configDir.get();
                Path path = Path.of(configDir);
                if (devServicesConfig.configClassPath) {
                    container.withClasspathResourceMapping(configDir, "/" + path.getFileName(), BindMode.READ_ONLY);
                    log.infof("MockServer configuration class-path directory '%s' mount to '/%s' container directory.",
                            configDir,
                            path.getFileName());
                } else {
                    if (Files.isDirectory(path)) {
                        container.withFileSystemBind(configDir, "/" + path.getFileName(), BindMode.READ_ONLY);
                        log.infof("MockServer configuration local directory '%s' mount to '/%s' container directory.",
                                configDir,
                                path.getFileName());
                    } else {
                        log.warnf("MockServer configuration local directory '%s' is not directory.", configDir);
                    }
                }
            }

            // enable test-container reuse
            if (devServicesConfig.reuse) {
                container.withReuse(true);
            }

            // start test-container
            container.start();

            return new DevServicesResultBuildItem.RunningDevService(FEATURE_NAME, container.getContainerId(),
                    new ContainerShutdownCloseable(container, FEATURE_NAME),
                    Map.of(MockServerConfig.HOST, container.getDevHost(),
                            MockServerConfig.PORT, "" + container.getDevPort(),
                            MockServerConfig.ENDPOINT, container.getDevEndpoint(),
                            MockServerConfig.CLIENT_HOST, container.getHost(),
                            MockServerConfig.CLIENT_PORT, "" + container.getServerPort()));
        };

        return containerLocator.locateContainer(devServicesConfig.serviceName, devServicesConfig.shared, launchMode)
                .map(containerAddress -> {

                    return new DevServicesResultBuildItem.RunningDevService(FEATURE_NAME, containerAddress.getId(),
                            null,
                            Map.of(MockServerConfig.HOST, containerAddress.getHost(),
                                    MockServerConfig.PORT, "" + containerAddress.getPort(),
                                    MockServerConfig.CLIENT_HOST, containerAddress.getHost(),
                                    MockServerConfig.CLIENT_PORT, "" + containerAddress.getPort(),
                                    MockServerConfig.ENDPOINT, String.format("http://%s:%d",
                                            containerAddress.getHost(), containerAddress.getPort())));
                })
                .orElseGet(defaultMockServerSupplier);
    }

    private static class QuarkusPortMockServerContainer extends MockServerContainer {
        private final OptionalInt fixedExposedPort;
        private final boolean useSharedNetwork;

        private String hostName = null;

        public QuarkusPortMockServerContainer(DockerImageName dockerImageName, OptionalInt fixedExposedPort, String serviceName,
                boolean useSharedNetwork) {
            super(dockerImageName);
            this.fixedExposedPort = fixedExposedPort;
            this.useSharedNetwork = useSharedNetwork;

            if (serviceName != null) {
                withLabel(DEV_SERVICE_LABEL, serviceName);
            }
        }

        @Override
        protected void configure() {
            super.configure();

            //TODO: add static final + configuration

            if (useSharedNetwork) {
                hostName = ConfigureUtil.configureSharedNetwork(this, FEATURE_NAME);
                return;
            } else {
                withNetwork(Network.SHARED);
            }

            if (fixedExposedPort.isPresent()) {
                addFixedExposedPort(fixedExposedPort.getAsInt(), MOCKSERVER_EXPOSED_PORT);
            } else {
                addExposedPort(MOCKSERVER_EXPOSED_PORT);
            }
        }

        public String getDevEndpoint() {
            if (useSharedNetwork) {
                return String.format("http://%s:%d", hostName, MOCKSERVER_EXPOSED_PORT);
            }
            return super.getEndpoint();
        }

        public int getDevPort() {
            if (useSharedNetwork) {
                return MOCKSERVER_EXPOSED_PORT;
            }

            if (fixedExposedPort.isPresent()) {
                return fixedExposedPort.getAsInt();
            }
            return super.getServerPort();
        }

        public String getDevHost() {
            return useSharedNetwork ? hostName : super.getHost();
        }
    }
}
