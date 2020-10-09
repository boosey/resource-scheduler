package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
// import org.jboss.logging.Logger;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class OwnerQuery {
    // private static final Logger log = Logger.getLogger(OwnerQuery.class);

    public Uni<List<Owner>> listAll() {
        return new UniCreateWithEmitter<List<Owner>>( emitter ->  
            emitter.complete(Owner.listAll()));
    }

    public Uni<Boolean> existsByName(String name) {
        return Owner.existsByName(name);
    }

    public long count() {
        return Owner.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(Owner.count("ownerId", id) > 0);
    }

    public Uni<Owner> findByName(String name) {
        return Owner.findByName(name);
    }

}