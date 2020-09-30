package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import org.jboss.logging.Logger;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;

public class ResourceQueryProcessor {
    private static final Logger log = Logger.getLogger(ResourceQueryProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "resourceAdded", responseSource = "handleResourceAdded", responseType = "resourceQueryable")
    @Transactional
    public String handleResourceAdded(ResourceAddedEventData evtData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        log.info("resource in query processor: ");

        val r = new Resource(); 
        r.setId(evtData.getResourceId());
        r.setName(evtData.getName());
        r.setActive(evtData.getActive());
        r.persist();

        log.info(" after persist resource in query processor: ");

        return "Complete";
    }

    @Funq
    @CloudEventMapping(trigger = "allResourcesDeleted", responseSource = "handleAllResourcesDeleted", responseType = "resourceQueryable")
    @Transactional
    public String handleAllResourcesDeleted(AllResourcesDeletedEventData evtData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        log.info("delete all in query processor: ");
        Resource.deleteAll();
        
        return "Complete";
    }

    @Funq
    @CloudEventMapping(trigger = "resourceDeleted", responseSource = "handleResourceDeleted", responseType = "resourceQueryable")
    @Transactional
    public String handleResourceDeleted(ResourceDeletedEventData evtData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        log.info("delete all in query processor: ");
        Resource.deleteById(evtData.getId());
        
        return "Complete";
    }


}