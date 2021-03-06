package boosey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/availability-query")
@RegisterRestClient(configKey="availability-query")
public interface AvailabilityQueryClient {

    @GET
    @Path("/")    
    @Produces(MediaType.APPLICATION_JSON)
    Response listAll();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    Response getAvailability(@PathParam("id") String id);    

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)       
    Response count();

    @GET
    @Path("/exists/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    Response exists(@PathParam("id") String id);

    @GET
    @Path("/availabilityForResource/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    Response availabilityForResource(@PathParam("id") String id);
}
