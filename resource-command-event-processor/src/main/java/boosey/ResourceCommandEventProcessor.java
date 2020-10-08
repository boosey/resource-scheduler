package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(ResourceCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_RESOURCE")
    public void handleAddResource(Resource resource, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Resource>(
            Type.RESOURCE_ADDED,
            Source.HANDLE_ADD_RESOURCE,
            resource)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_RESOURCES")
    public void handleDeleteAllResources(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_RESOURCES_DELETED,
            Source.HANDLE_DELETE_ALL_RESOURCES,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_RESOURCE")
    public void handleDeleteResource(Resource resource, @Context CloudEvent evtCtx) {

        new ResourceSchedulerEvent<Resource>(
            Type.RESOURCE_DELETED,
            Source.HANDLE_DELETE_RESOURCE,
            resource)
            .fire();
    }

}