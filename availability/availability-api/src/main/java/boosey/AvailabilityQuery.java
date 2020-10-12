package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import boosey.datatype.availability.Availability;
// import org.jboss.logging.Logger;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class AvailabilityQuery {
    // private static final Logger log = Logger.getLogger(AvailabilityQuery.class);

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
}