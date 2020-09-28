package boosey;

import org.jboss.logging.Logger;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;

public class ResourceCommands {
    private static final Logger log = Logger.getLogger(ResourceCommands.class);

    @Funq
    @CloudEventMapping(trigger = "addResource", responseSource = "handleAddResource", responseType = "addedResource")
    public String handleAddResource(AddResourceEventData evtData, @Context CloudEvent evtCtx) {

        ResourceCommandsEventRecord evtRec = ResourceCommandsEventRecord.buildWith(evtCtx, evtData);
        evtRec.persist();

        log.info("*** add resource *** separated evtData: " + evtData);
        return evtData + "::" + "annotatedChain";
    }

}