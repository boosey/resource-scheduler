package boosey;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import boosey.owner.Owner;

@Path("/owner-command")
@RegisterRestClient(configKey="owner-command")
public interface OwnerCommandClient {

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)    
    @Produces(MediaType.APPLICATION_JSON)
    Response addOwner(Owner owner);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)    
    @Produces(MediaType.APPLICATION_JSON)
    Response replaceOwner(@PathParam("id") String id, Owner owner);

    @DELETE
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)    
    Response deleteAllOwner();

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)      
    Response deleteOwner(@PathParam("id") String id);    
}
