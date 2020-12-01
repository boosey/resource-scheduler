package boosey;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import boosey.owner.Owner;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.val;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

// @Slf4j
@Path("/owner-command")
@ApplicationScoped
public class OwnerCommand {

    private static Jsonb jsonb = JsonbBuilder.create();

    @Inject @RestClient OwnerQueryClient query;

    @Inject
    @GrpcService("eventsservicegrpc")                     
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOwner(Owner owner) {
        try {
            if (!existsByName(owner)) { 

                val request = FireRequest.newBuilder()
                                .setType(EventType.ADD_OWNER)
                                .setSource(EventSource.OWNER_API)
                                .setEventData(jsonb.toJson(owner))
                                .build();

                val reply = grpcEvents.fire(request);
                return Response.ok(reply.getEventId()).build();
                        
            } else {
                return Response.status(Status.CONFLICT).build();            
            }            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }    

    private Boolean existsByName(Owner o) {
        return query.existsByName(o.getName()).readEntity(Boolean.class);
    }

    private Boolean exists(String ownerId) {
        return query.exists(ownerId).readEntity(Boolean.class);
    }

    @PUT
    @Path("/{ownerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public Boolean replaceOwner(@PathParam("ownerId") String ownerId, Owner owner) {
        if (exists(ownerId)) {
            if (!owner.id.equalsIgnoreCase(ownerId))
                throw new RuntimeException("IDs aren't equal");     
            
            val request = FireRequest.newBuilder()
                            .setType(EventType.REPLACE_OWNER)
                            .setSource(EventSource.OWNER_API)
                            .setEventData(jsonb.toJson(owner))
                            .build();

            grpcEvents.fire(request);            

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }   

    @DELETE
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)    
    public Boolean deleteAllOwners() {
            val request = FireRequest.newBuilder()
                            .setType(EventType.DELETE_ALL_OWNERS)
                            .setSource(EventSource.OWNER_API)
                            .setEventData(jsonb.toJson(NoEventData.builder().build()))
                            .build();

            grpcEvents.fire(request);                   

        return true;
    }    

    @DELETE
    @Path("/{ownerId}")
    @Produces(MediaType.TEXT_PLAIN)    
    public Boolean deleteOwner(@PathParam("ownerId") String ownerId) {
        if (exists(ownerId)) { 
            FireRequest request = FireRequest.newBuilder()
                            .setType(EventType.DELETE_OWNER)
                            .setSource(EventSource.OWNER_API)
                            .setEventData(jsonb.toJson(ItemIdData.builder()
                                            .id(ownerId)
                                            .build()))
                            .build();

            grpcEvents.fire(request);                    

        } else {     
            throw new NotFoundException("Owner does not exist");
        }
        
        return true;
    }    
}