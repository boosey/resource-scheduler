package boosey;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import boosey.reservation.Reservation;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class ReservationQueryEventProcessor {

    private static Jsonb jsonb = JsonbBuilder.create();        

    @Funq
    @CloudEventMapping(trigger = "RESERVATION_ADDED")
    @Transactional
    public void handleReservationAdded(EventData eventData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Reservation resIn =  jsonb.fromJson(eventData.value, Reservation.class);
        Reservation r = new Reservation();
        r.setId(resIn.getId());
        r.setResourceId(resIn.getResourceId());
        r.setReserverId(resIn.getReserverId());
        r.setStartTime(resIn.getStartTime());
        r.setEndTime(resIn.getEndTime());
        r.setState(resIn.getState());
        r.persist();
     
    }

    @Funq
    @CloudEventMapping(trigger = "RESERVATION_REPLACED")
    @Transactional
    public void handleReservationReplaced(Reservation reservation, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        // Reservation r = Reservation.findById(reservation.getId());

    }

    @Funq
    @CloudEventMapping(trigger = "ALL_RESERVATIONS_DELETED")
    @Transactional
    public void handleAllReservationsDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Reservation.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "RESERVATION_DELETED")
    @Transactional
    public void handleReservationDeleted(ItemIdData reservationId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Reservation.deleteById(reservationId.getId()); 
    }
}