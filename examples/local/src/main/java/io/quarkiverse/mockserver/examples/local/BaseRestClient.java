package io.quarkiverse.mockserver.examples.local;

import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/activity/data")
@RegisterRestClient(configKey = "activity-client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BaseRestClient {

    @POST
    @Path("1")
    Map<String, Object> getData1(Map<String, Object> data);

    @POST
    @Path("2")
    Response getData2(Map<String, Object> data);
}
