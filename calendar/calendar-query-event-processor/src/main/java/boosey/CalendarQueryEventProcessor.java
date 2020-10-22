package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;

import boosey.calendar.Calendar;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class CalendarQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "CALENDAR_ADDED")
    @Transactional
    public void handleCalendarAdded(Calendar calendar, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        val r = new Calendar(); 
        r.setId(calendar.getId());

        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "CALENDAR_REPLACED")
    @Transactional
    public void handleCalendarReplaced(Calendar calendar, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        // Calendar r = Calendar.findById(calendar.getId());

    }

    @Funq
    @CloudEventMapping(trigger = "ALL_CALENDARS_DELETED")
    @Transactional
    public void handleAllCalendarsDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Calendar.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "CALENDAR_DELETED")
    @Transactional
    public void handleCalendarDeleted(ItemIdData calendarId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Calendar.deleteById(calendarId.getId()); 
    }
}