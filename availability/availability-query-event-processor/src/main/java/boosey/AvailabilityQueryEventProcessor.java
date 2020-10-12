package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import boosey.datatype.availability.Availability;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class AvailabilityQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "OWNER_ADDED")
    @Transactional
    public void handleAvailabilityAdded(Availability availability, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        val r = new Availability(); 
        r.setId(availability.getId());

        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "OWNER_REPLACED")
    @Transactional
    public void handleAvailabilityReplaced(Availability availability, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        // Availability r = Availability.findById(availability.getId());

    }

    @Funq
    @CloudEventMapping(trigger = "ALL_OWNERS_DELETED")
    @Transactional
    public void handleAllAvailabilitysDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Availability.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "OWNER_DELETED")
    @Transactional
    public void handleAvailabilityDeleted(ItemIdData availabilityId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Availability.deleteById(availabilityId.getId()); 
    }
}