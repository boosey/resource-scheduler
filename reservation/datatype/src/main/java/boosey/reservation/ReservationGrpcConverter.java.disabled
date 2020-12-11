package boosey.reservation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.enterprise.context.ApplicationScoped;
import com.google.protobuf.Timestamp;
import boosey.ReservationCommon.ReservationGrpc;
import boosey.ReservationCommon.ReservationGrpc.Builder;


@ApplicationScoped
public class ReservationGrpcConverter {
    
    public ReservationGrpc convertToGrpc(Reservation r) {
        
        Builder r1 = ReservationGrpc.newBuilder();

            if (r.getId() != null)
                r1.setId(r.getId());

            if (r.getResourceId() != null)
                r1.setResourceId(r.getResourceId());

            if (r.getReserverId() != null)
                r1.setReserverId(r.getReserverId());

            if (r.getState() != null)
                r1.setState(r.getState().name());  
                
            if (r.getStartTime() != null)            
                r1.setStartTime(timestampFromLocalDateTime(r.getStartTime()));

            if (r.getEndTime() != null)            
                r1.setEndTime(timestampFromLocalDateTime(r.getEndTime()));

        return r1.build();
    }

    private Timestamp timestampFromLocalDateTime(LocalDateTime ldt) {

        return ldt != null 

                ? Timestamp.newBuilder()
                    .setSeconds(
                        ldt.toEpochSecond(
                            ZoneOffset.of(ZoneOffset.systemDefault().getId()))
                    )
                    .build()

                : null;
    }
    
}
