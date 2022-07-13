package io.quarkiverse.mockserver.it.base;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
