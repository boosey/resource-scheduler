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
import boosey.resource.Availability;
import boosey.resource.Resource;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/resources")
public class ResourceAPI {

    @Inject @RestClient ResourceQueryClient query;
    @Inject @RestClient ResourceCommandClient command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> listAll() {
        try {
            return Uni.createFrom().item(query.listAll());    
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
    public Uni<Resource> findByName(@QueryParam("name") String name) {
        return Uni.createFrom().item(query.findByName(name).readEntity(Resource.class));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addResource(Resource resource) {

        log.info("add: " + resource.toString());

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                String resourceId = command.addResource(resource).readEntity(String.class);
                emitter.complete(Response.accepted(resourceId).build());

            } 
            catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            } 
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }        
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{resourceId}")
    public Uni<Response> replaceResource(@PathParam("resourceId") String resourceId, 
                                            Resource resource) {
        
        log.info("command.replaceResource");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceResource(resourceId, resource);
                emitter.complete(Response.ok(resourceId).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteAllResources() {

            val r = query.count();
            command.deleteAllResources();
            return Uni.createFrom().item(Response.ok(r.readEntity(Long.class)).build());        
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{resourceId}")
    public Uni<Response> deleteResource(@PathParam("resourceId") String resourceId) {
        
        log.info("command.deleteResource");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteResource(resourceId);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    // Availability API

    @POST
    @Path("/{resourceId}/availability")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addAvailability(@PathParam("resourceId") String resourceId, 
                            Availability availability) {

            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(command.addResourceAvailability(resourceId, availability))
                    .build());
    }   

    @GET
    @Path("/availability")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> listAllAvailability() {
        try { 
            return Uni.createFrom().item(query.listAllAvailability());    
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }    

    @GET
    @Path("/{resourceId}/availability")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> listResourceAvailability(@PathParam("resourceId") String resourceId) {
        try { 
            return Uni.createFrom().item(query.listResourceAvailability(resourceId));    
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }    

}