package boosey.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.protobuf.Timestamp;

import boosey.ReservationCommon.State;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
@Entity
public class Reservation extends PanacheEntityBase {
    @Id public String id = UUID.randomUUID().toString();
    public String resourceId;
    public String reserverId;
    public Timestamp startTime;
    public Timestamp endTime;
    public State state;

    public Reservation() {
        state = State.RESERVATION_STATE_UNKNOWN;
    }

    public void setStateUknown() {
        state = State.RESERVATION_STATE_UNKNOWN;
    }
    public void setRequested() {
        state = State.RESERVATION_REQUESTED;
    }
    public void setAccepted() {
        state = State.RESERVATION_STATE_UNKNOWN;
    }
    public void setDenied() {
        state = State.RESERVATION_DENIED;
    }

    public boolean isStateUnknown() {return state == State.RESERVATION_STATE_UNKNOWN;}    
    public boolean isRequested() {return state == State.RESERVATION_REQUESTED;}    
    public boolean isAccepted() {return state == State.RESERVATION_ACCEPTED;}    
    public boolean isDenied() {return state == State.RESERVATION_DENIED;}    

    private static PanacheQuery<Reservation> findByResourceIdQuery(String resourceId) {
        return Reservation.find("resourceId = ?1", resourceId);
    }

    public static Uni<List<Reservation>> findByResourceId(String resourceId){
        return new UniCreateWithEmitter<List<Reservation>>(emitter -> {

            val reservationQuery = findByResourceIdQuery(resourceId);
            emitter.complete(reservationQuery.list());           
        });           
    }
}
