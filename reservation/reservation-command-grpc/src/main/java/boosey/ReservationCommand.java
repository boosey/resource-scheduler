package boosey;

import java.util.UUID;
import java.util.function.Supplier;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotAcceptableException;
import com.google.protobuf.ByteString;

import boosey.ReservationCommon.ReservationGrpc;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ActivateRequestContext
public class ReservationCommand extends MutinyReservationCommandServiceGrpc.ReservationCommandServiceImplBase  {

    @Inject
    @GrpcService("eventsservicegrpc")
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    

    @Inject
    @GrpcService("reservations_query_grpc")
    MutinyReservationQueryServiceGrpc.MutinyReservationQueryServiceStub reservationsQuery;  

    
    private <T> Uni<T> prepareReply(Supplier<T> supplier) {
        log.info("in prepare reply");
        return Uni.createFrom()
            .item(() -> supplier.get())
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    private Uni<Boolean> checkIfReservationExists(String id) {
        log.info("in checkIfReservationExists");
        return reservationsQuery
            .exists(ExistsRequest.newBuilder().setId(id).build())
            .runSubscriptionOn(Infrastructure.getDefaultExecutor())
            .onItem()
            .transform(r -> {
                log.info("transforming reply to Boolean");
                return Boolean.valueOf(r.getExists());
            });
    }    

    private void invokeIfExistsOrElse(String id, Runnable exists, Runnable doesntExist) {

        log.info("in invokeIfExists");
        checkIfReservationExists(id).subscribe().with(
            itemExists -> {
                if (itemExists) {
                    log.info("run exists function");
                    exists.run();
                } else { 
                    log.info("run doesntExist function");
                    doesntExist.run();
                }
            }      
        );     
    }    

    private void fireOnEventService2(EventType type, EventSource source, ByteString eventData) {
        log.info("calling grpcEvents 2");
        grpcEvents.fire2(FireRequest.newBuilder()
            .setType(type)
            .setSource(source)
            .setEventData(eventData.toStringUtf8())
            .build());
    }

    public Uni<AddReservationReply> add(AddReservationRequest request) {

        log.info("in command add");
        ReservationGrpc r = ReservationGrpc.newBuilder(request.getReservation())
                                .setId(UUID.randomUUID().toString())
                                .build();

        invokeIfExistsOrElse(
            r.getId(), 
            () -> { 
                    log.info("throwing not acceptable");
                    throw new NotAcceptableException();
                  }, 
            () -> { 
                    log.info("about to fire ADD_RESERVATION");
                    log.info("start: " + r.getStartTime());
                    log.info("end: " + r.getEndTime());
                    log.info("bytestring: " + r.toByteString());
                    fireOnEventService2(
                        EventType.ADD_RESERVATION, 
                        EventSource.RESERVATION_API, 
                        r.toByteString());

                    log.info("after fire");
                  }
        );

        log.info("about preparing reply and returning");
        return prepareReply(() -> 
                AddReservationReply.newBuilder()
                    .setId(r.getId())
                    .build());
    }       
}
