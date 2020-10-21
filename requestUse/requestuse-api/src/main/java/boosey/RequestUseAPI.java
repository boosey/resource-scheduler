package boosey;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/requestuse")
@ApplicationScoped
public class RequestUseAPI {

    @Inject RequestUseQuery query;
    @Inject RequestUseCommand command;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<RequestUse>> listAll() {
        return query.listAll();
    }

    @GET
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/createtestdata")
    public Boolean createtestdata() {

        RequestUse r = new RequestUse();
        r.resourceId = UUID.randomUUID().toString();
        r.resourceActive = true;
        r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 6, 00);
        r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 18, 00);
        r.persist();

        r = new RequestUse();
        r.resourceId = UUID.randomUUID().toString();
        r.resourceActive = true;
        r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 10, 00);
        r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 18, 00);
        r.persist();

        r = new RequestUse();
        r.resourceId = UUID.randomUUID().toString();
        r.resourceActive = true;
        r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 8, 00);
        r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 20, 00);
        r.persist();

    return true;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addRequestUse(RequestUse requestUse) {

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                String requestUseId = command.addRequestUse(requestUse);
                emitter.complete(Response.accepted(requestUseId).build());

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }            
        });        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{requestUseId}")
    public Uni<Response> replaceRequestUse(@PathParam("requestUseId") String requestUseId, 
                                            RequestUse requestUse) {
        
        log.info("command.replaceRequestUse");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.replaceRequestUse(requestUseId, requestUse);
                emitter.complete(Response.ok(requestUseId).build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }

    // @DELETE
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Uni<Response> deleteAllRequestUse() {

    //     return new UniCreateWithEmitter<Response>( emitter -> {
    //         val cnt = query.count();
    //         command.deleteAllRequestUse();
    //         emitter.complete(Response.ok(cnt).build());
    //     });
    // }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{requestUseId}")
    public Uni<Response> deleteRequestUse(@PathParam("requestUseId") String requestUseId) {
        
        log.info("command.deleteRequestUse");

        return new UniCreateWithEmitter<Response>( emitter -> {
            try {
                command.deleteRequestUse(requestUseId);
                emitter.complete(Response.ok("1").build());  

            } catch (NotAcceptableException e) {
                emitter.complete(Response.status(Status.NOT_FOUND).build());
            }
        });
    }
}