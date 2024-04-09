package io.quarkiverse.mockserver.examples.local;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BaseRestController {

    @Inject
    @RestClient
    BaseRestClient client;

    @POST
    @Path("1")
    public Map<String, Object> post(Data data) {
        return client.getData1(Map.of(data.key, data.value));
    }

    @POST
    @Path("2")
    public Response post2(Data data) {
        try (Response response = client.getData2(Map.of(data.key, data.value))) {
            String tmp = response.readEntity(String.class);
            return Response.ok(tmp).build();
        } catch (WebApplicationException ex) {
            return Response.fromResponse(ex.getResponse()).build();
        }
    }

    public static class Data {
        public String key;
        public String value;
    }
}
