package boosey;

import java.util.List;

import io.smallrye.mutiny.Uni;

public interface QueryBaseInterface<T> {
    public Uni<List<T>> listAll();
}
