package boosey;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.resource.Availability;
import boosey.resource.Resource;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@Path("/resource-command")
@ApplicationScoped
public class ResourceCommandRest {

    @Inject @RestClient ResourceQueryClient query;

    @Inject
    @GrpcService("eventsservicegrpc")
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)    
    @Produces(MediaType.APPLICATION_JSON)    
    public String addResource(Resource resource) {
        try {
            query.existsByName(resource.getName()).getStatus();
            throw new NotAcceptableException();            

        } catch (WebApplicationException e) {

            if (e.getResponse().getStatus() == 404) {
            log.info("posted resource converted to string: " + Resource.jsonbNoAvailability().toJson(resource));

                grpcEvents.fire(FireRequest.newBuilder()
                            .setType(EventType.ADD_RESOURCE)
                            .setSource(EventSource.RESOURCE_API)
                            .setEventData(Resource.jsonbNoAvailability().toJson(resource))
                            .build());  
            } else {
                e.printStackTrace();
                throw e;
            }
        }
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
                        .setEventData(Resource.jsonbNoAvailability().toJson(resource))
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
                    .setEventData(Resource.jsonbNoAvailability().toJson(NoEventData.builder().build()))
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
                        .setEventData(Resource.jsonbNoAvailability().toJson(ItemIdData.builder().id(resourceId).build()))
                        .build());  
        } else {        
            throw new NotAcceptableException("Resource Does Not Exist");
        }

        return true;
    }    

    // Availability API

    @POST
    @Path("/{resourceId}/availability/")
    @Produces(MediaType.TEXT_PLAIN)    
    public String addResourceAvailability(@PathParam("resourceId") String resourceId, 
                        Availability availability) {
        try {
            // If the resource cannot be found, then an WebException is thrown
            Resource r = query.getResource(resourceId).readEntity(Resource.class);

            availability.setResource(r);

            FireReply reply = grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.ADD_AVAILABILITY)
                        .setSource(EventSource.RESOURCE_API)
                        .setEventData(Availability.jsonbResourceIdOnly().toJson(availability))
                        .build());   
                        
            return reply.getEventId();

        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() != 404) {
                e.printStackTrace();
            }
            throw e;
        }
    }    
}