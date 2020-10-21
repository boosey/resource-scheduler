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
import boosey.acceptuse.AcceptUse;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/acceptuse")
@ApplicationScoped
public class AcceptUseAPI {

    @Inject AcceptUseQuery query;
    @Inject AcceptUseCommand command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<AcceptUse>> listAll() {
        return query.listAll();
    }

    // @GET
    // @Transactional
    // @Produces(MediaType.TEXT_PLAIN)
    // @Path("/createtestdata")
    // public Boolean createtestdata() {

    //     AcceptUse r = new AcceptUse();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 6, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 18, 00);
    //     r.persist();

    //     r = new AcceptUse();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 10, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 18, 00);
    //     r.persist();

    //     r = new AcceptUse();
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
    public Uni<Response> addAcceptUse(AcceptUse acceptUse) {

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                String acceptUseId = command.addAcceptUse(acceptUse);
                emitter.complete(Response.accepted(acceptUseId).build());

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }            
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{acceptUseId}")
    public Uni<Response> replaceAcceptUse(@PathParam("acceptUseId") String acceptUseId, 
                                            AcceptUse acceptUse) {
        
        log.info("command.replaceAcceptUse");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceAcceptUse(acceptUseId, acceptUse);
                emitter.complete(Response.ok(acceptUseId).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    // @DELETE
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Uni<Response> deleteAllAcceptUse() {

    //     return new UniCreateWithEmitter<Response>( emitter -> {
    //         val cnt = query.count();
    //         command.deleteAllAcceptUse();
    //         emitter.complete(Response.ok(cnt).build());
    //     });
    // }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{acceptUseId}")
    public Uni<Response> deleteAcceptUse(@PathParam("acceptUseId") String acceptUseId) {
        
        log.info("command.deleteAcceptUse");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteAcceptUse(acceptUseId);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }
}