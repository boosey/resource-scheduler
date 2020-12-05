package boosey;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;
import boosey.reservation.Reservation;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ActivateRequestContext
public class ReservationQuery extends MutinyReservationQueryServiceGrpc.ReservationQueryServiceImplBase  {
    
    private <T> Uni<T> prepareReply(Supplier<T> supplier) {

        return Uni.createFrom()
            .item(() -> supplier.get())
            .runSubscriptionOn(Infrastructure.getDefaultExecutor())
            .onItem()
                .ifNull()
                .failWith(new StatusRuntimeException(Status.NOT_FOUND));
    }

    @Override
    public Uni<ListAllReply> listAll(ListAllRequest request) {

        return prepareReply( 
            () -> {
                return ListAllReply.newBuilder().addAllReservations(
                    Reservation.<Reservation>streamAll()
                        .map((r) -> {
                                return ReservationGrpcQ.newBuilder()
                                    .setId(r.getId())
                                    .setResourceId(r.getResourceId())
                                    .setReserverId(r.getReserverId())
                                    // .setStartTime(r.getStartTime().toString())
                                    // .setEndTime(r.getEndTime().toString())
                                    .setState(r.getState().name())
                                    .build();                    
                        })
                        .collect(Collectors.toList())
                    )
                    .build();
            }            
        );
    }

    @Override 
    public Uni<GetReply> get(GetRequest request) {

        return prepareReply( 
            () -> { 
                log.info("find id: " + request.getId());
                Reservation r = Reservation.<Reservation>findById(request.getId());
                log.info("reservation: " + r);

                if (r != null) {
                    log.info("item not null returning a reply");
                    return GetReply.newBuilder()
                        .setReservation(
                            ReservationGrpcQ.newBuilder()
                            .setId(r.getId())
                            .setResourceId(r.getResourceId())
                            .setReserverId(r.getReserverId())
                            // .setStartTime(r.getStartTime().toString())
                            // .setEndTime(r.getEndTime().toString())
                            .setState(r.getState().name())
                            .build()                                         
                        )
                        .build();
                } else {
                    log.info("item is null - returning null");
                    return null;
                }
            }
        );    
    }   

    @Override 
    public Uni<CountReply> count(CountRequest request) {

        log.info("in grpc count()");
        return prepareReply(
                    () -> CountReply.newBuilder()
                        .setCount(Reservation.count())
                        .build()
                );             
    }

    @Override 
    public Uni<ExistsReply> exists(ExistsRequest request) {

        return prepareReply( 
            () -> ExistsReply.newBuilder()
                .setExists(Reservation.count("id", request.getId()) > 0)
                .build()
        );      
    }

}
