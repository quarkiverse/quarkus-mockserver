# Quarkus MockServer

<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![Build](https://github.com/quarkiverse/quarkus-mockserver/workflows/Build/badge.svg?branch=main)](https://github.com/quarkiverse/quarkus-mockserver/actions?query=workflow%3ABuild)
[![License](https://img.shields.io/github/license/quarkiverse/quarkus-mockserver.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Central](https://img.shields.io/maven-central/v/io.quarkiverse.mockserver/quarkus-mockserver-parent?color=green)](https://search.maven.org/search?q=g:io.quarkiverse.mockserver%20AND%20a:quarkus-mockserver-parent)
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

Quarkus [MockServer](https://mock-server.com/) extension for tests and local development.

## Usage

To use this extension for local development, add the dependency to the target project:
```xml
<dependency>
    <groupId>io.quarkiverse.mockserver</groupId>
    <artifactId>quarkus-mockserver</artifactId>
    <scope>provided</scope>
</dependency>
```

[Example project](examples/local)

### Configuration

```properties
# enable or disable mockserver devservices
quarkus.mockserver.devservices.enabled=true|false
# mockserver configuration properties file, container file '/config/mockserver.properties'
quarkus.mockserver.devservices.config-file=src/test/resources/mockserver.properties
# mockserver configuration directory for the expectation initializers, container directory '/<last_name_from_path>'.
quarkus.mockserver.devservices.config-dir=src/test/resources/mockserver
# enable or disable logs of the mockserver container
quarkus.mockserver.devservices.log=true|false
# mockserver docker image name
quarkus.mockserver.devservices.image-name="mockserver/mockserver:5.15.0"
# mockserver container fix port
quarkus.mockserver.devservices.port=
# container shared mode
quarkus.mockserver.devservices.shared=true|false
# mockserver devservices service name
quarkus.mockserver.devservices.service-name=mockserver
# enable reusable mockserver test-container (https://www.testcontainers.org/features/reuse/)
quarkus.mockserver.devservices.reuse=false
```
Runtime configuration values will be set up during start of the mockserver container. These values could be used to configure rest-client in your application.
```properties
# mockserver endpoint is random port http://localhost:44556 or http://mockserver:1080 for shared containers. 
quarkus.mockserver.endpoint=
# mockserver port is random testcontainers port or 1080 for shared containers
quarkus.mockserver.port=
# mockserver port is localhost or mockserver for shared containers
quarkus.mockserver.host=
# mockserver host for the test client, docker host
quarkus.mockserver.client.host=
# mockserver port for the test client, testcontainers random port
quarkus.mockserver.client.port=
```
Rest client configuration example:
```properties
%dev.activity-client/mp-rest/url=${quarkus.mockserver.endpoint}
%test.activity-client/mp-rest/url=${quarkus.mockserver.endpoint}
```

MockServer documentation:
* Configuration properties file [link](https://mock-server.com/mock_server/configuration_properties.html)
* Expectation Initializer JSON [link](https://mock-server.com/mock_server/initializing_expectations.html#expectation_initializer_json)

## Testing 

To use the extension for test, add the dependency to the target project:
```xml
<dependency>
    <groupId>io.quarkiverse.mockserver</groupId>
    <artifactId>quarkus-mockserver-test</artifactId>
    <scope>test</scope>
</dependency>
```

Test example
```java
import io.quarkiverse.mockserver.test.MockServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTest
@QuarkusTestResource(MockServerTestResource.class)
public class BaseTest extends AbstractTest {

}
```
We can reuse the test for the integration test.
```java
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class BaseIT extends BaseTest {

}
```
For more information check examples in the `integration-tests` directory in this repo.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://www.lorislab.org"><img src="https://avatars2.githubusercontent.com/u/828045?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Andrej Petras</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkiverse-mockserver/commits?author=andrejpetras" title="Code">💻</a> <a href="#maintenance-andrejpetras" title="Maintenance">🚧</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification.
Contributions of any kind welcome!
