package boosey;

import java.util.List;

import javax.ws.rs.GET;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;

public class RESTApiBase<T extends PanacheEntityBase> implements RESTApiBaseInterface {


    protected QueryBase<T> query;

    @GET
    public Uni<List<?>> listAll() {
        return query.listAll();
    }
}