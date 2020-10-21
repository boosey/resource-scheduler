package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUseCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(RequestUseCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_OWNER")
    public void handleAddRequestUse(RequestUse requestUse, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<RequestUse>(
            Type.OWNER_ADDED,
            Source.HANDLE_ADD_OWNER,
            requestUse)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_OWNER")
    public void handleReplaceRequestUse(RequestUse requestUse, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<RequestUse>(
            Type.OWNER_REPLACED,
            Source.HANDLE_REPLACE_OWNER,
            requestUse)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_OWNERS")
    public void handleDeleteAllRequestUses(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_OWNERS_DELETED,
            Source.HANDLE_DELETE_ALL_OWNERS,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_OWNER")
    public void handleDeleteRequestUse(ItemIdData requestUseId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteRequestUse: " + requestUseId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.OWNER_DELETED,
            Source.HANDLE_DELETE_OWNER,
            requestUseId)
            .fire();
    }

}