package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.resource.Resource;
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
    @CloudEventMapping(trigger = "REPLACE_RESOURCE")
    public void handleReplaceResource(Resource resource, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Resource>(
            Type.RESOURCE_REPLACED,
            Source.HANDLE_REPLACE_RESOURCE,
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
    public void handleDeleteResource(ItemIdData resourceId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteResource: " + resourceId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.RESOURCE_DELETED,
            Source.HANDLE_DELETE_RESOURCE,
            resourceId)
            .fire();
    }

}