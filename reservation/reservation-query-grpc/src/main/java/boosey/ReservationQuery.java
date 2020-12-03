package boosey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;
import boosey.reservation.Reservation;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ActivateRequestContext
public class ReservationQuery extends MutinyReservationQueryServiceGrpc.ReservationQueryServiceImplBase  {
    
    private <T> Uni<T> prepareReply(Supplier<T> supplier) {
        log.info("in prepare reply");
        return Uni.createFrom()
            .item(() -> supplier.get())
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @Override
    public Uni<ListAllReply> listAll(ListAllRequest request) {

        List<ReservationGrpc> reservationsGrpc = new ArrayList<>();

        return prepareReply( 
            () -> {
                Reservation.<Reservation>streamAll()
                    .forEach(r -> {
                        reservationsGrpc.add(
                            ReservationGrpc.newBuilder()
                                .setId(r.getId())
                                .setResourceId(r.getResourceId())
                                .setReserverId(r.getReserverId())
                                .setStartTime(r.getStartTime().toString())
                                .setEndTime(r.getEndTime().toString())
                                .build()
                        );
                    });

                ListAllReply reply = ListAllReply.newBuilder()
                        .addAllReservations(reservationsGrpc)
                        .build();
                
                return reply;
            }            
        );
    }

    @Override 
    public Uni<GetReply> get(GetRequest request) {

        return prepareReply( 
            () -> { 
                Reservation r = Reservation.<Reservation>findById(request.getId());

                return GetReply.newBuilder()
                    .setReservation(
                        ReservationGrpc.newBuilder()
                        .setId(r.getId())
                        .setResourceId(r.getResourceId())
                        .setReserverId(r.getReserverId())
                        .setStartTime(r.getStartTime().toString())
                        .setEndTime(r.getEndTime().toString())
                        .setState(r.getState())
                        .build()                                         
                    )
                    .build();
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
