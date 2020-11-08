package boosey;

import javax.inject.Inject;
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
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import boosey.owner.Owner;

@Slf4j
@Path("/owners")
public class OwnerAPI {

    @Inject @RestClient OwnerQueryClient query;
    @Inject @RestClient OwnerCommandClient command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> listAll() {
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
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/existsByName")
    public Uni<Boolean> existsByName(@QueryParam("name") String name) {
        return Uni.createFrom().item(query.existsByName(name).readEntity(Boolean.class));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/findByName")
    public Uni<Owner> findByName(@QueryParam("name") String name) {
        return Uni.createFrom().item(query.findByName(name).readEntity(Owner.class));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addOwner(Owner owner) {
        try {
            val r = command.addOwner(owner);
            if (r.getStatusInfo() == Status.OK)
                return Uni.createFrom().item(
                            Response
                                .accepted(r.readEntity(String.class))
                                .build()); 
            else
                return Uni.createFrom().item(
                            Response
                            .status(Status.CONFLICT)
                            .build());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }      
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
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> deleteAllOwners() {
            val r = query.count();
            command.deleteAllOwner();
            return Uni.createFrom().item(Response.ok(r.readEntity(Long.class)).build());
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