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
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import boosey.datatype.owner.Owner;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/owners")
public class OwnerAPI {

    @Inject OwnerQuery query;
    @Inject OwnerCommand command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Owner>> listAll() {
        return query.listAll();
    }

    @GET
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/createtestdata")
    public Boolean createtestdata() {

        Owner r = new Owner();
        r.setName("John");
        r.persist();

        r = new Owner();
        r.setName("Kelly");
        r.persist();

        r = new Owner();
        r.setName("Emily");
        r.persist();

        r = new Owner();
        r.setName("Zach");
        r.persist();

        return true;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/existsByName")
    public Uni<Boolean> existsByName(@QueryParam("name") String name) {
        return query.existsByName(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/findByName")
    public Uni<Owner> findByName(@QueryParam("name") String name) {
        return query.findByName(name);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addOwner(Owner owner) {

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                String ownerId = command.addOwner(owner);
                emitter.complete(Response.accepted(ownerId).build());

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }            
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{ownerId}")
    public Uni<Response> replaceOwner(@PathParam("ownerId") String ownerId, 
                                            Owner owner) {
        
        log.info("command.replaceOwner");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceOwner(ownerId, owner);
                emitter.complete(Response.ok(ownerId).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteAllOwners() {

        return new UniCreateWithEmitter<Response>( emitter -> {
            val cnt = query.count();
            command.deleteAllOwners();
            emitter.complete(Response.ok(cnt).build());
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{ownerId}")
    public Uni<Response> deleteOwner(@PathParam("ownerId") String ownerId) {
        
        log.info("command.deleteOwner");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteOwner(ownerId);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }
}