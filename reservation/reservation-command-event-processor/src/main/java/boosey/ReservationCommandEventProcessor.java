package boosey;

import javax.inject.Inject;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(ReservationCommandEventProcessor.class);
    
    @Inject
    @GrpcService("eventsservicegrpc")                     
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    
    
    @Funq
    @CloudEventMapping(trigger = "ADD_RESERVATION")
    public void handleAddReservation(EventData eventData, @Context CloudEvent eventContext) {

        log.info("incoming string: " + eventData);
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.RESERVATION_ADDED)
                        .setSource(EventSource.HANDLE_ADD_RESERVATION)
                        .setEventData(eventData.value)
                        .build());               
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_RESERVATION")
    public void handleReplaceReservation(EventData eventData, @Context CloudEvent eventContext) {

        log.info("incoming string: " + eventData);
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.RESERVATION_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_RESERVATION)
                        .setEventData(eventData.value)
                        .build());               
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_RESERVATIONS")
    public void handleDeleteAllReservations(EventData eventData, @Context CloudEvent eventContext) {

        log.info("incoming string: " + eventData);
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.ALL_RESERVATIONS_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_ALL_RESERVATIONS)
                        .setEventData(eventData.value)
                        .build());                 
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_RESERVATION")
    public void handleDeleteReservation(EventData eventData, @Context CloudEvent evtCtx) {

        log.info("incoming string: " + eventData);
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.RESERVATION_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_RESERVATION)
                        .setEventData(eventData.value)
                        .build());              
    }

}