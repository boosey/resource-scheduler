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
    public void handleResourceAdded(Resource resource, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        val r = new Resource(); 
        r.setResourceId(resource.getResourceId());
        r.setName(resource.getName());
        r.setActive(resource.getActive());
        r.persist();
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
    public void handleResourceDeleted(ItemIdData resourceId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        log.info("query processor: " + resourceId);
        Resource.deleteById(resourceId.getId());
        
    }


}