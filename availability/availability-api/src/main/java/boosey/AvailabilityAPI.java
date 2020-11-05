package boosey;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/availability")
@ApplicationScoped
public class AvailabilityAPI {

    @Inject @RestClient AvailabilityQueryClient query;    
    @Inject @RestClient AvailabilityCommandClient command;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> listAll() {
        // val r = clients.getAvailabilityQueryClient().listAll();        
        val r = query.listAll();
        if (r.getStatusInfo() == Status.OK) {
            return Uni.createFrom().item(
                Response
                    .ok(r.getEntity())
                    .build()
            );
        } else {
            throw new RuntimeException();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Uni<Availability> getAvailability(@PathParam("id") String id) {
        
         val r = query.getAvailability(id);
         if (r.getStatusInfo() == Status.NOT_FOUND) {
             throw new NotFoundException();
         } 
         return Uni.createFrom().item((Availability)r.getEntity());
    }      

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> count() {

        return Uni.createFrom().item(
                Response
                    .ok(query.count().getEntity())
                    .build()
            );
    }    

    @GET
    @Path("/exists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> exists(@PathParam("id") String id) {

        return Uni.createFrom().item(query.exists(id));
    }    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addAvailability(Availability availability) {

        return new UniCreateWithEmitter<Response>( emitter -> {

            val r = command.addAvailability(availability);
            if (r.getStatusInfo() == Status.OK)
                emitter.complete(Response.accepted(r.readEntity(String.class)).build());
            else
                emitter.complete(Response.status(Status.CONFLICT).build());
           
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<Response> replaceAvailability(@PathParam("id") String id, 
                                            Availability availability) {
        
        log.info("Api.replaceAvailability");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceAvailability(id, availability);
                emitter.complete(Response.ok(id).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteAllAvailability() {

        return new UniCreateWithEmitter<Response>( emitter -> {
            val r = query.count();
            command.deleteAllAvailability();
            emitter.complete(Response.ok(r.getEntity()).build());
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<Response> deleteAvailability(@PathParam("id") String id) {
        
        log.info("Api.deleteAvailability");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteAvailability(id);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }
}