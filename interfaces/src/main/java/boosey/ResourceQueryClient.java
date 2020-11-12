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
    @Path("/exists/{id}")
    @Produces(MediaType.APPLICATION_JSON)      
    public Response exists(@PathParam("id") String id); 

    @GET
    @Path("/findByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Response findByName(@PathParam("resourceName") String name); 
}
