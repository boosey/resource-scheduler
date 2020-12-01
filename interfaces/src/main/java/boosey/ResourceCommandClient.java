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

import boosey.resource.Availability;
import boosey.resource.Resource;

@Path("/resource-command")
@RegisterRestClient(configKey="resource-command")
public interface ResourceCommandClient {

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)    
    @Produces(MediaType.APPLICATION_JSON)
    Response addResource(Resource resource);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)    
    @Produces(MediaType.APPLICATION_JSON)
    Response replaceResource(@PathParam("id") String id, Resource resource);

    @DELETE
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)    
    Response deleteAllResources();

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)      
    Response deleteResource(@PathParam("id") String id);    

    // Availability API

    @POST
    @Path("/{resourceId}/availability/")
    @Produces(MediaType.APPLICATION_JSON)    
    public String addResourceAvailability(@PathParam("resourceId") String resourceId, 
                        Availability availability);    

                        
}
