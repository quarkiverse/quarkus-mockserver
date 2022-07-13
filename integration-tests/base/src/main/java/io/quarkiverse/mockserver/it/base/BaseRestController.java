package io.quarkiverse.mockserver.it.base;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        try {
            return Response.fromResponse(client.getData2(Map.of(data.key, data.value))).build();
        } catch (WebApplicationException ex) {
            return Response.fromResponse(ex.getResponse()).build();
        }
    }

    public static class Data {
        public String key;
        public String value;
    }
}
