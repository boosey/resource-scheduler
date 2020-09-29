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
        r.eventRecordId = evtData.recordId.toString();
        r.name = evtData.name;
        r.available = evtData.available;
        r.persist();

        log.info(" after persist resource in query processor: ");

        return "Complete";
    }
}