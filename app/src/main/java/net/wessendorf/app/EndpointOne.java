package net.wessendorf.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/one")
public class EndpointOne {

    @GET
    public Response log() {
        return Response.ok("KC-Endpoint One: (" + this+ ")").build();
    }
}
