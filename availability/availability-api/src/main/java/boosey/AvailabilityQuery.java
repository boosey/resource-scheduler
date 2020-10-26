package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class AvailabilityQuery {

    public Uni<List<Availability>> listAll() {
        return new UniCreateWithEmitter<List<Availability>>( emitter ->  
            emitter.complete(Availability.listAll()));
    }

    public long count() {
        return Availability.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(Availability.count("availabilityId", id) > 0);
    }

    public Uni<List<Availability>> listResourceAvailability(String resourceId) {

        return Availability.findByResourceId(resourceId);
    }
}