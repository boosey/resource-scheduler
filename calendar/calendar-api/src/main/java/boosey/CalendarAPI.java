package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import boosey.calendar.Calendar;
import io.smallrye.mutiny.Uni;

@Path("/calendar")
@ApplicationScoped
public class CalendarAPI {

    @Inject CalendarQuery query;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Calendar>> listAll() {
        return query.listAll();
    }

    // @GET
    // @Transactional
    // @Produces(MediaType.TEXT_PLAIN)
    // @Path("/createtestdata")
    // public Boolean createtestdata() {

    //     Calendar r = new Calendar();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 6, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 18, 00);
    //     r.persist();

    //     r = new Calendar();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 10, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 21, 18, 00);
    //     r.persist();

    //     r = new Calendar();
    //     r.resourceId = UUID.randomUUID().toString();
    //     r.resourceActive = true;
    //     r.startTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 8, 00);
    //     r.endTime = LocalDateTime.of(2020, Month.OCTOBER, 20, 20, 00);
    //     r.persist();

    // return true;
    // }
    
}