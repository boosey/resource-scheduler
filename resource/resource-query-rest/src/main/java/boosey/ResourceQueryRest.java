package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import boosey.resource.Resource;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@Path("/resource-query")
@ApplicationScoped
public class ResourceQueryRest {

    @GET
    @Path("/")    
    @Produces(MediaType.APPLICATION_JSON)    
    public Uni<List<Resource>> listAll() {
        return new UniCreateWithEmitter<List<Resource>>( emitter ->  
            emitter.complete(Resource.listAll()));
    }

    @GET
    @Path("/existsByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Boolean> existsByName(String name) {
        return Resource.existsByName(name);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Uni<Resource> getResource(@PathParam("id") String id) {
        Resource r = Resource.<Resource>findById(id);
        if (r != null)
            return Uni.createFrom().item(r);
        else
            throw new NotFoundException();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)    
    public long count() {
        return Resource.count();
    }

    @GET
    @Path("/exists/{id}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(Resource.count("resourceId", id) > 0);
    }

    @GET
    @Path("/findByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Resource> findByName(String name) {
        return Resource.findByName(name);
    }

}