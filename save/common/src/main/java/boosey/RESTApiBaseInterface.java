package boosey;

import java.util.List;
import io.smallrye.mutiny.Uni;

public interface RESTApiBaseInterface {

    public Uni<List<?>> listAll();
    
}
