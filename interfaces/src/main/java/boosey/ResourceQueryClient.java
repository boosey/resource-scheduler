package boosey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/resource-query")
@RegisterRestClient(configKey="resource-query")
public interface ResourceQueryClient {

    @GET
    @Path("/")    
    @Produces(MediaType.APPLICATION_JSON)
    Response listAll();

    @GET
    @Path("/existsByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)  
    Response existsByName(@PathParam("resourceName") String name); 

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    Response getResource(@PathParam("id") String id);    

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)     
    public Response count(); 

    @GET
    @Path("/{id}/exists")
    @Produces(MediaType.APPLICATION_JSON)      
    public Response exists(@PathParam("id") String id); 

    @GET
    @Path("/findByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Response findByName(@PathParam("resourceName") String name); 

    // Availability API
    @GET
    @Path("/availability")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllAvailability();
    
    @GET
    @Path("/{resourceId}/availability")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response listResourceAvailability(@PathParam("resourceId") String resourceId);

    @GET
    @Path("/availability/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Response getAvailability(@PathParam("id") String id);

    @GET
    @Path("/availability/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countAllAvailability();

    @GET
    @Path("/{resourceId}/availability/count")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response countResourceAvailability(@PathParam("resourceId") String resourceId);
     
    @GET
    @Path("/availability/exists/{id}")
    @Produces(MediaType.TEXT_PLAIN)       
    public Response existsAvailability(@PathParam("id") String id);
     
    @GET
    @Path("/availabilityForResource/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Response availabilityForResource(@PathParam("id") String id);

}
