package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.protobuf.Timestamp;

import boosey.reservation.Reservation;
import boosey.reservation.ReservationCustomAdapter;
import io.grpc.StatusRuntimeException;
import io.grpc.Status.Code;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.extern.slf4j.Slf4j;
import io.smallrye.mutiny.Uni;
import boosey.ReservationCommon.ReservationGrpc;
import boosey.ReservationCommon.State;

@Slf4j
@Path("/reservations")
@ApplicationScoped
public class ReservationAPI {

    @Inject
    @GrpcService("reservations_query_grpc")
    MutinyReservationQueryServiceGrpc.MutinyReservationQueryServiceStub reservationsQuery;  

    @Inject
    @GrpcService("reservations_command_grpc")
    MutinyReservationCommandServiceGrpc.MutinyReservationCommandServiceStub reservationsCommand;  

    @Inject
    ReservationCustomAdapter reservationAdapter;

    @GET
    public Uni<List<ReservationGrpc>> listAll() {
        return reservationsQuery
            .listAll(ListAllRequest.getDefaultInstance())
            .onItem()
                .transform(r -> r.getReservationsList());
    }

    @GET
    @Path("/{id}")
    public Uni<ReservationGrpc> getReservation(@PathParam("id") String id) {

        return 
            reservationsQuery
                .get(GetRequest.newBuilder().setId(id).build())
                .onFailure(StatusRuntimeException.class)
                    .transform((e) -> {
                        if (((StatusRuntimeException)e).getStatus().getCode() == Code.NOT_FOUND)
                            return new NotFoundException();
                        else
                            return new RuntimeException();
                    })
                .onItem()
                    .transform(r -> r.getReservation());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Uni<Long> count() {
        try {
            return 
                reservationsQuery
                    .count(CountRequest.getDefaultInstance())
                    .onItem()
                    .transform(cr -> Long.valueOf(cr.getCount()));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }  
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}/exists")
    public Uni<Boolean> exists(@PathParam("id") String id) {
        try {
            return 
                reservationsQuery
                    .exists(ExistsRequest.newBuilder().setId(id).build())
                    .onItem()
                    .transform(r -> Boolean.valueOf(r.getExists()));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    public Uni<Response> addReservation(Reservation reservation) {

        log.info("add Reservation");
        // The transform at the end creates the Uni<Response> after
        // getting a reply from the add call
        // When RESTEasy subscribes the pipeline flows
        return reservationsCommand.add(

                    AddReservationRequest.newBuilder()
                        .setReservation(ReservationGrpc.newBuilder()
                            .setId(reservation.getId())
                            .setResourceId(reservation.getResourceId())
                            .setReserverId(reservation.getReserverId())  
                            .setStartTime(Timestamp.getDefaultInstance())  
                            .setEndTime(Timestamp.getDefaultInstance())
                            .setState(State.RESERVATION_REQUESTED)
                        )
                        .build()
                )
                .onFailure(StatusRuntimeException.class)
                    .transform((e) -> {
                        if (((StatusRuntimeException)e).getStatus().getCode() == Code.ALREADY_EXISTS)
                            return new WebApplicationException(Response.Status.CONFLICT);
                        else
                            return new RuntimeException();
                    })                    
                .onItem() // Got reply from add
                    .transform((reply) -> {
                        return Response.accepted(reply.getId()).build();
                    });
    }
    
    // @PUT
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // @Path("/{reservationId}")
    // public Uni<Response> replaceReservation(@PathParam("reservationId") String reservationId, 
    //                                         Reservation reservation) {
        
    //     log.info("command.replaceReservation");

    //     return new UniCreateWithEmitter<Response>( emitter -> {
    //         try {
    //             command.replaceReservation(reservationId, reservation);
    //             emitter.complete(Response.ok(reservationId).build());  

    //         } catch (NotAcceptableException e) {
    //             emitter.complete(Response.status(Status.NOT_FOUND).build());
    //         }
    //     });
    // }

    // @DELETE
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Uni<Response> deleteAllReservation() {

    //     return new UniCreateWithEmitter<Response>( emitter -> {
    //         val cnt = query.count();
    //         command.deleteAllReservation();
    //         emitter.complete(Response.ok(cnt).build());
    //     });
    // }

    // @DELETE
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // @Path("/{reservationId}")
    // public Uni<Response> deleteReservation(@PathParam("reservationId") String reservationId) {
        
    //     log.info("command.deleteReservation");

    //     return new UniCreateWithEmitter<Response>( emitter -> {
    //         try {
    //             command.deleteReservation(reservationId);
    //             emitter.complete(Response.ok("1").build());  

    //         } catch (NotAcceptableException e) {
    //             emitter.complete(Response.status(Status.NOT_FOUND).build());
    //         }
    //     });
    // }
}