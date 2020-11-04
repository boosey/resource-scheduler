package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;
import lombok.val;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Slf4j
@Path("/availability-command")
@ApplicationScoped
public class AvailabilityCommand {

    @Inject @RestClient EventServiceClient events;
    @Inject @RestClient AvailabilityQueryClient query;

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String addAvailability(Availability availability) {

        // TODO Need to check if the availability slot already exists
        // and check that the times don't overlap with an existing one
        // perhaps consolidating overlapping entries
        // validate start < end
        if (Uni.createFrom().item(false)
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("Availability Exists");

        } else {
            log.info("in add availability");
            val e = new ResourceSchedulerEvent<Availability>(
                        Type.ADD_AVAILABILITY,
                        Source.AVAILABILITY_API,
                        availability);    
            log.info("after constructing event");
            events.fire(e);
            log.info("after fire");
        }

        return availability.getId();
    }    

    @PUT
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean replaceAvailability(@PathParam("id") String id, Availability availability) {

        // Also make validation checks
        Response r = query.exists(id);
        if (r.getStatusInfo() == Status.OK) {
    
            // Just in case
            availability.setId(id);

            val e = new ResourceSchedulerEvent<Availability>(
                        Type.REPLACE_AVAILABILITY,
                        Source.AVAILABILITY_API,
                        availability); 
                
            events.fire(e);                   

        } else {        
            throw new NotAcceptableException("Availability Does Not Exist");
        }

        return true;
    }   

    @DELETE
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)    
    public Uni<Long> deleteAllAvailability() {

        val c = query.count().readEntity(Long.class);

        val e = new ResourceSchedulerEvent<NoEventData>(
            Type.DELETE_ALL_AVAILABILITIES,
            Source.AVAILABILITY_API,
            new NoEventData()); 

        events.fire(e);
        log.info("after delete all fire");

        return Uni.createFrom().item(c);
    }    

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)      
    public Boolean deleteAvailability(String id) {

        val r = query.exists(id);
        if (r.getStatusInfo() == Status.OK) {
            log.info("exists entity: " + r.getEntity());

            val e = new ResourceSchedulerEvent<ItemIdData>(
                Type.DELETE_AVAILABILITY,
                Source.AVAILABILITY_API,
                ItemIdData.builder().id(id).build());             
 
            events.fire(e);                   
        } else {        
            throw new NotAcceptableException("Availability Does Not Exist");
        }

        return true;
    }    
}