package boosey;

import java.time.LocalDateTime;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import boosey.ReservationCommon.ReservationGrpc;
import boosey.reservation.Reservation;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "RESERVATION_ADDED")
    @Transactional
    public void handleReservationAdded(EventData eventData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, InvalidProtocolBufferException {

        log.info("saving Reservation");

        ReservationGrpc resIn =  ReservationGrpc.parseFrom(ByteString.copyFromUtf8(eventData.getValue()));
        Reservation r = new Reservation();
        r.setId(resIn.getId());
        r.setResourceId(resIn.getResourceId());
        r.setReserverId(resIn.getReserverId());
        r.setStartTime(LocalDateTime.parse(resIn.getStartTime()));
        r.setEndTime(LocalDateTime.parse(resIn.getEndTime()));
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