package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import boosey.reservation.Reservation;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/reservation")
@ApplicationScoped
public class ReservationAPI {

    @Inject ReservationQuery query;
    @Inject ReservationCommand command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Reservation>> listAll() {
        return query.listAll();
    }

    // @GET
    // @Transactional
    // @Produces(MediaType.TEXT_PLAIN)
    // @Path("/createtestdata")
    // public Boolean createtestdata() {

    //     Reservation r = new Reservation();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 6, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 18, 00);
    //     r.persist();

    //     r = new Reservation();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 10, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 18, 00);
    //     r.persist();

    //     r = new Reservation();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 8, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 20, 00);
    //     r.persist();

    // return true;
    // }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addReservation(Reservation reservation) {

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                String reservationId = command.addReservation(reservation);
                emitter.complete(Response.accepted(reservationId).build());

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }            
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{reservationId}")
    public Uni<Response> replaceReservation(@PathParam("reservationId") String reservationId, 
                                            Reservation reservation) {
        
        log.info("command.replaceReservation");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceReservation(reservationId, reservation);
                emitter.complete(Response.ok(reservationId).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteAllReservation() {

        return new UniCreateWithEmitter<Response>( emitter -> {
            val cnt = query.count();
            command.deleteAllReservation();
            emitter.complete(Response.ok(cnt).build());
        });
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{reservationId}")
    public Uni<Response> deleteReservation(@PathParam("reservationId") String reservationId) {
        
        log.info("command.deleteReservation");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteReservation(reservationId);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }
}