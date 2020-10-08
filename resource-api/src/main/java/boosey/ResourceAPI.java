package boosey;

import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/resources")
public class ResourceAPI {

    @Inject ResourceQuery query;
    @Inject ResourceCommand command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Resource>> listAll() {
        return query.listAll();
    }

    @GET
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/createtestdata")
    public Boolean createtestdata() {

        Resource r = new Resource();
        r.setName("Space 402");
        r.setActive("true");
        r.persist();

        r = new Resource();
        r.setName("Space 401");
        r.setActive("true");
        r.persist();

        r = new Resource();
        r.setName("Space 403");
        r.setActive("true");
        r.persist();

        r = new Resource();
        r.setName("Space 404");
        r.setActive("true");
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
    public Uni<Resource> findByName(@QueryParam("name") String name) {
        return query.findByName(name);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addResource(Resource resource) {

        return new UniCreateWithEmitter<Response>( emitter -> {
            String resourceId = command.addResource(resource);
            emitter.complete(Response.accepted(resourceId).build());
        });        
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteAllResources() {

        log.info("entering deleteAllReources API");
        return new UniCreateWithEmitter<Response>( emitter -> {
            log.info("about to call command.deleteAllResources");

            command.deleteAllResources();
            emitter.complete(Response.ok().build());
        });
    }
}