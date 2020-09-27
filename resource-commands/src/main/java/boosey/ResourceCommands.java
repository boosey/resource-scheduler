package boosey;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import org.jboss.logging.Logger;

public class ResourceCommands {
    private static final Logger log = Logger.getLogger(ResourceCommands.class);

    @Funq
    @CloudEventMapping(trigger = "addResource", responseSource = "handleAddResource", responseType = "addedResource")
    public String handleAddResource(AddResourceEventData evtData, @Context CloudEvent event) {
        log.info("*** add resource *** evtData: " + evtData + event);
        return evtData + "::" + "annotatedChain";
    }

}