package boosey;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import boosey.availability.Availability;

@Path("/availability-command")
@RegisterRestClient(configKey="availability-command")
public interface AvailabilityCommandClient {

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addAvailability(Availability availability);

    @PUT
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response replaceAvailability(@PathParam("id") String id, Availability availability);

    @DELETE
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)    
    public Response deleteAllAvailability();

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)      
    public Response deleteAvailability(@PathParam("id") String id);    
}
