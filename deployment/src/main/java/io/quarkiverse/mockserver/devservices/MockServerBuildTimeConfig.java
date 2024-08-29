package io.quarkiverse.mockserver.devservices;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
@ConfigMapping(prefix = "quarkus.mockserver")
public interface MockServerBuildTimeConfig {

    /**
     * Default Dev services configuration.
     */
    @WithParentName
    DevServiceConfiguration defaultDevService();

    interface DevServiceConfiguration {
        /**
         * Configuration for DevServices
         * <p>
         * DevServices allows Quarkus to automatically start MockServer in dev and test mode.
         */
        @WithName("devservices")
        DevServicesConfig devservices();

    }
}
