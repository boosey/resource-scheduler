package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "RESOURCE_ADDED")
    @Transactional
    public void handleResourceAdded(Resource owner, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        val r = new Resource(); 
        r.setResourceId(owner.getResourceId());
        r.setName(owner.getName());
        r.setActive(owner.getActive());
        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "RESOURCE_REPLACED")
    @Transactional
    public void handleResourceReplaced(Resource owner, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        if (!owner.isPersistent()) {
            log.info("Not Persistent on UPDATE: " + owner);
            Resource r = Resource.findById(owner.getResourceId());
            r.setName(owner.getName());
            r.setActive(owner.getActive());
        } else {
            // If already persistent, then the end of 
            // transaction updates the DB automatically
        }
    }

    @Funq
    @CloudEventMapping(trigger = "ALL_RESOURCES_DELETED")
    @Transactional
    public void handleAllResourcesDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Resource.deleteAll();

    }

    @Funq
    @CloudEventMapping(trigger = "RESOURCE_DELETED")
    @Transactional
    public void handleResourceDeleted(ItemIdData ownerId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        log.info("query processor: " + ownerId);
        Resource.deleteById(ownerId.getId());
        
    }


}