package io.quarkiverse.mockserver.devservices;

import java.util.List;

import io.quarkiverse.mockserver.runtime.MockServerConfig;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Consume;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.RuntimeConfigSetupCompleteBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;

public class DevUIMockServerProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    @Consume(RuntimeConfigSetupCompleteBuildItem.class)
    public CardPageBuildItem pages(List<DevServicesResultBuildItem> devServicesResultBuildItemList,
            MockServerBuildTimeConfig config) {

        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem();

        if (devServicesResultBuildItemList != null) {
            var mockServerService = devServicesResultBuildItemList.stream()
                    .filter(x -> config.defaultDevService().devservices().serviceName().equals(x.getName()))
                    .findFirst().orElse(null);
            if (mockServerService != null) {
                var keys = mockServerService.getConfig();
                var url = String.format("http://%s:%s/mockserver/dashboard",
                        keys.get(MockServerConfig.CLIENT_HOST),
                        keys.get(MockServerConfig.CLIENT_PORT));

                cardPageBuildItem.addPage(Page.externalPageBuilder("Dashboard")
                        .url(url).isHtmlContent()
                        .icon("font-awesome-solid:sliders"));

                cardPageBuildItem.addPage(Page.externalPageBuilder("External dashboard")
                        .url(url, url).doNotEmbed()
                        .icon("font-awesome-solid:share-from-square"));
            }
        }

        return cardPageBuildItem;
    }
}
