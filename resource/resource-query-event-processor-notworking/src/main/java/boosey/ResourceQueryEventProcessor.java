package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import boosey.resource.Resource;
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
    public void handleResourceAdded(Resource resource, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        log.info("q added: " + resource.toString());

        val r = new Resource(); 
        r.setId(resource.getId());
        r.setOwnerId(resource.getOwnerId());
        r.setName(resource.getName());
        r.setActive(resource.getActive());
        r.setAvailableByDefault(resource.getAvailableByDefault());
        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "RESOURCE_REPLACED")
    @Transactional
    public void handleResourceReplaced(Resource resource, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        if (!resource.isPersistent()) {
            log.info("Not Persistent on UPDATE: " + resource);
            Resource r = Resource.findById(resource.getId());
            r.setName(resource.getName());
            r.setActive(resource.getActive());
        } else {
            // If already persistent, then the end of 
            // transaction updates the DB automatically
        }
    }

    @Funq
    @CloudEventMapping(trigger = "ALL_RESOURCES_DELETED")
    @Transactional
    public void handleAllResourcesDeleted(NoEventData nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        Resource.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "RESOURCE_DELETED")
    @Transactional
    public void handleResourceDeleted(ItemIdData resourceId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        Resource.deleteById(resourceId.getId()); 
    }
}