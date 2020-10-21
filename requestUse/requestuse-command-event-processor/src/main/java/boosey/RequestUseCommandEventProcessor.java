package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.requestuse.RequestUse;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUseCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(RequestUseCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_REQUEST_USE")
    public void handleAddRequestUse(RequestUse requestUse, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<RequestUse>(
            Type.REQUEST_USE_ADDED,
            Source.HANDLE_ADD_REQUEST_USE,
            requestUse)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_REQUESTUSE")
    public void handleReplaceRequestUse(RequestUse requestUse, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<RequestUse>(
            Type.REQUEST_USE_REPLACED,
            Source.HANDLE_REPLACE_REQUEST_USE,
            requestUse)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_REQUESTUSES")
    public void handleDeleteAllRequestUses(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_REQUEST_USES_DELETED,
            Source.HANDLE_DELETE_ALL_REQUEST_USES,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_REQUESTUSE")
    public void handleDeleteRequestUse(ItemIdData requestUseId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteRequestUse: " + requestUseId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.REQUEST_USE_DELETED,
            Source.HANDLE_DELETE_REQUEST_USE,
            requestUseId)
            .fire();
    }

}