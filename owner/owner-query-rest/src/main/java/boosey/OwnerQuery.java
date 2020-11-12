package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import boosey.owner.Owner;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@Path("/owner-query")
@ApplicationScoped
public class OwnerQuery {

    @GET
    @Path("/")    
    @Produces(MediaType.APPLICATION_JSON)    
    public Uni<List<Owner>> listAll() {
        return new UniCreateWithEmitter<List<Owner>>( emitter ->  
            emitter.complete(Owner.listAll()));
    }

    @GET
    @Path("/existsByName/{ownerName}")    
    @Produces(MediaType.APPLICATION_JSON)    
    public Uni<Boolean> existsByName(@PathParam("ownerName") String name) {
        return Owner.existsByName(name);
    }

    @GET
    @Path("/count")    
    @Produces(MediaType.APPLICATION_JSON)    
    public Uni<Long> count() {
        return Uni.createFrom().item(Owner.count());
    }

    @GET
    @Path("/exists/{id}")    
    @Produces(MediaType.APPLICATION_JSON)    
    public Uni<Boolean> exists(@PathParam("id") String id) {
        return Uni.createFrom().item(Owner.count("ownerId", id) > 0);
    }

    @GET
    @Path("/exists/{ownerName}")    
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Owner> findByName(@PathParam("ownerName") String name) {
        return Owner.findByName(name);
    }
}