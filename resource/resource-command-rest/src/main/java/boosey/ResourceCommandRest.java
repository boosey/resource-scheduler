package boosey;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.resource.Resource;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.DELETE;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.google.gson.Gson;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@Path("/resource-command")
@ApplicationScoped
public class ResourceCommandRest {

    private static Gson gson = new Gson();

    @Inject @RestClient ResourceQueryClient query;

    @Inject
    @GrpcService("eventsservicegrpc")
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)    
    public String addResource(Resource resource) {

        // if (query.existsByName(resource.getName()).readEntity(Boolean.class)) {

        //     throw new NotAcceptableException("Resource Exists");

        // } else {    
            
            log.info("adding resource: " + resource);
            try {
                grpcEvents.fire(FireRequest.newBuilder()
                            .setType(EventType.ADD_RESOURCE)
                            .setSource(EventSource.RESOURCE_API)
                            .setEventData(gson.toJson(resource))
                            .build());                 
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
                    
        // }

        return resource.getId();
    }    

    @PUT
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean replaceResource(String resourceId, Resource resource) {

        if (query.exists(resourceId).readEntity(Boolean.class)) {
    
            // Just in case
            resource.setId(resourceId);
 
            grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.REPLACE_RESOURCE)
                        .setSource(EventSource.RESOURCE_API)
                        .setEventData(gson.toJson(resource))
                        .build());              

        } else {        
            throw new NotAcceptableException("Resource Does Not Exist");
        }
        return true;
    }   

    @DELETE
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN) 
    public Boolean deleteAllResources() {

        grpcEvents.fire(FireRequest.newBuilder()
                    .setType(EventType.DELETE_ALL_RESOURCES)
                    .setSource(EventSource.RESOURCE_API)
                    .setEventData(gson.toJson(NoEventData.builder().build()))
                    .build());          

        return true;
    }    

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)     
    public Boolean deleteResource(String resourceId) {

        if (query.exists(resourceId).readEntity(Boolean.class)) {
      
        grpcEvents.fire(FireRequest.newBuilder()
                    .setType(EventType.DELETE_RESOURCE)
                    .setSource(EventSource.RESOURCE_API)
                    .setEventData(gson.toJson(ItemIdData.builder().id(resourceId).build()))
                    .build());  
        } else {        
            throw new NotAcceptableException("Resource Does Not Exist");
        }

        return true;
    }    
}