package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import boosey.requestuse.RequestUse;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class RequestUseQuery {

    public Uni<List<RequestUse>> listAll() {
        return new UniCreateWithEmitter<List<RequestUse>>( emitter ->  
            emitter.complete(RequestUse.listAll()));
    }

    public long count() {
        return RequestUse.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(RequestUse.count("requestUseId", id) > 0);
    }
}