package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import boosey.acceptuse.AcceptUse;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class AcceptUseQuery {

    public Uni<List<AcceptUse>> listAll() {
        return new UniCreateWithEmitter<List<AcceptUse>>( emitter ->  
            emitter.complete(AcceptUse.listAll()));
    }

    public long count() {
        return AcceptUse.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(AcceptUse.count("acceptUseId", id) > 0);
    }
}