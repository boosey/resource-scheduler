package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.reservation.Reservation;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(ReservationCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_RESERVATION")
    public void handleAddReservation(Reservation reservation, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Reservation>(
            Type.RESERVATION_ADDED,
            Source.HANDLE_ADD_RESERVATION,
            reservation)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_RESERVATION")
    public void handleReplaceReservation(Reservation reservation, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Reservation>(
            Type.RESERVATION_REPLACED,
            Source.HANDLE_REPLACE_RESERVATION,
            reservation)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_RESERVATIONS")
    public void handleDeleteAllReservations(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_AVAILABILITIES_DELETED,
            Source.HANDLE_DELETE_ALL_AVAILABILITIES,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_RESERVATION")
    public void handleDeleteReservation(ItemIdData reservationId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteReservation: " + reservationId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.RESERVATION_DELETED,
            Source.HANDLE_DELETE_RESERVATION,
            reservationId)
            .fire();
    }

}