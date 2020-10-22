package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import boosey.reservation.Reservation;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class ReservationQuery {

    public Uni<List<Reservation>> listAll() {
        return new UniCreateWithEmitter<List<Reservation>>( emitter ->  
            emitter.complete(Reservation.listAll()));
    }

    public long count() {
        return Reservation.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(Reservation.count("reservationId", id) > 0);
    }
}