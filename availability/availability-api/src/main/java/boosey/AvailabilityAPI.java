package boosey;

import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import boosey.datatype.availability.Availability;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/availabilitys")
public class AvailabilityAPI {

    @Inject AvailabilityQuery query;
    @Inject AvailabilityCommand command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Availability>> listAll() {
        return query.listAll();
    }

    @GET
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/createtestdata")
    public Boolean createtestdata() {

        //  TODO
        // Availability r = new Availability();

        return true;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addAvailability(Availability availability) {

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                String availabilityId = command.addAvailability(availability);
                emitter.complete(Response.accepted(availabilityId).build());

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }            
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{availabilityId}")
    public Uni<Response> replaceAvailability(@PathParam("availabilityId") String availabilityId, 
                                            Availability availability) {
        
        log.info("command.replaceAvailability");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceAvailability(availabilityId, availability);
                emitter.complete(Response.ok(availabilityId).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteAllAvailabilitys() {

        return new UniCreateWithEmitter<Response>( emitter -> {
            val cnt = query.count();
            command.deleteAllAvailabilitys();
            emitter.complete(Response.ok(cnt).build());
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{availabilityId}")
    public Uni<Response> deleteAvailability(@PathParam("availabilityId") String availabilityId) {
        
        log.info("command.deleteAvailability");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteAvailability(availabilityId);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }
}