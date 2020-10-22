package boosey;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import boosey.calendar.Calendar;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;

@ApplicationScoped
public class CalendarQuery {

    public Uni<List<Calendar>> listAll() {
        return new UniCreateWithEmitter<List<Calendar>>( emitter ->  
            emitter.complete(Calendar.listAll()));
    }

    public long count() {
        return Calendar.count();
    }

    public Uni<Boolean> exists(String id) {
        return Uni.createFrom().item(Calendar.count("calendarId", id) > 0);
    }
}