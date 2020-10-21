package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import boosey.resource.Resource;
// import org.jboss.logging.Logger;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class ResourceQuery {
    // private static final Logger log = Logger.getLogger(ResourceQuery.class);

    public Uni<List<Resource>> listAll() {
        return new UniCreateWithEmitter<List<Resource>>( emitter ->  
            emitter.complete(Resource.listAll()));
    }

    public Uni<Boolean> existsByName(String name) {
        return Resource.existsByName(name);
    }

    public long count() {
        return Resource.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(Resource.count("resourceId", id) > 0);
    }

    public Uni<Resource> findByName(String name) {
        return Resource.findByName(name);
    }

}