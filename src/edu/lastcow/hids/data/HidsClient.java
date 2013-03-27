package edu.lastcow.hids.data;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/21/13
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface HidsClient {

    @POST
    @Path("/test")
    public Response testRest(@FormParam("name") String name, @FormParam("serial") String serial);
}
