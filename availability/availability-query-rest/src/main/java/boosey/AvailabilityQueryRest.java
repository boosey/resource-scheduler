package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
@Path("/availability-query")
@ApplicationScoped
public class AvailabilityQueryRest {

    @GET
    @Path("/")    
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Availability>> listAll() {
        return new UniCreateWithEmitter<List<Availability>>( emitter ->  {
                emitter.complete(Availability.listAll());
        });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Uni<Availability> getAvailability(@PathParam("id") String id) {

        Availability a = Availability.<Availability>findById(id);

        if (a != null) {
            return Uni.createFrom().item(a);
        } else {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Long> count() {
        return Uni.createFrom().item(Availability.count());
    }

    @GET
    @Path("/exists/{id}")
    @Produces(MediaType.TEXT_PLAIN)       
    public Uni<Response> exists(@PathParam("id") String id) {   
        if (Availability.count("id", id) > 0)
            return Uni.createFrom().item(Response.ok().build());
        else
            throw new NotFoundException();
    }

    @GET
    @Path("/availabilityForResource/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Uni<List<Availability>> availabilityForResource(@PathParam("id") String id) {
        return Availability.findByResourceId(id);
    }

}