package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.reservation.Reservation;
import io.smallrye.mutiny.Uni;

// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;

// @Slf4j
@ApplicationScoped
public class ReservationCommand {
    @Inject ReservationQuery query;

    public String addReservation(Reservation reservation) {

        // TODO Need to check if the reservation slot already exists
        if (Uni.createFrom().item(true)
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("Reservation Exists");

        } else {
            ResourceSchedulerEvent.builder()
                .eventType(Type.ADD_AVAILABILITY)
                .source(Source.AVAILABILITY_API)
                .eventData(reservation)
                .build()
                .fire();                 
        }

        return reservation.getId();
    }    


    public Boolean replaceReservation(String reservationId, Reservation reservation) {

        if (query.exists(reservationId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            reservation.setId(reservationId);

            ResourceSchedulerEvent.builder()
                .eventType(Type.REPLACE_AVAILABILITY)
                .source(Source.AVAILABILITY_API)
                .eventData(reservation)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Reservation Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllReservation() {

        ResourceSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_AVAILABILITIES)
            .source(Source.AVAILABILITY_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteReservation(String reservationId) {

        if (query.exists(reservationId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            ResourceSchedulerEvent.builder()
                .eventType(Type.DELETE_AVAILABILITY)
                .source(Source.AVAILABILITY_API)
                .eventData(ItemIdData.builder().id(reservationId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Reservation Does Not Exist");
        }

        return true;
    }    
}