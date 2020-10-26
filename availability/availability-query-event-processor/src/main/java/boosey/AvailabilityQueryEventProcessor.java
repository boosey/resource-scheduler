package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;

import boosey.availability.Availability;
// import boosey.owner.Owner;
import boosey.resource.Resource;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class AvailabilityQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "AVAILABILITY_ADDED")
    @Transactional
    public void handleAvailabilityAdded(Availability availability, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        val a = new Availability(); 
        a.setId(availability.getId());
        a.setResourceId(availability.getResourceId());

        Resource r = Resource.findById(availability.getResourceId());
        String rName = r.getName();
        a.setResourceName(rName);

        Owner o = Owner.findById(r.getOwnerId());
        a.setOwnerName(o.getName());

        a.setResourceActive(r.getActive());

        a.setStartTime(availability.getStartTime());
        a.setEndTime(availability.getEndTime());

        a.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "AVAILABILITY_REPLACED")
    @Transactional
    public void handleAvailabilityReplaced(Availability availability, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Availability a = Availability.findById(availability.getId());
        
        a.setResourceId(availability.getResourceId());

        Resource r = Resource.findById(availability.getResourceId());
        String rName = r.getName();
        a.setResourceName(rName);

        Owner o = Owner.findById(r.getOwnerId());
        a.setOwnerName(o.getName());

        a.setResourceActive(r.getActive());

        a.setStartTime(availability.getStartTime());
        a.setEndTime(availability.getEndTime());
    }

    @Funq
    @CloudEventMapping(trigger = "ALL_AVAILABILITIES_DELETED")
    @Transactional
    public void handleAllAvailabilitysDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Availability.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "AVAILABILITY_DELETED")
    @Transactional
    public void handleAvailabilityDeleted(ItemIdData availabilityId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Availability.deleteById(availabilityId.getId()); 
    }
}