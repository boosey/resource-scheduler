package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.acceptuse.AcceptUse;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AcceptUseCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(AcceptUseCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_ACCEPT_USE")
    public void handleAddAcceptUse(AcceptUse acceptUse, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<AcceptUse>(
            Type.ACCEPT_USE_ADDED,
            Source.HANDLE_ADD_ACCEPT_USE,
            acceptUse)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_ACCEPTUSE")
    public void handleReplaceAcceptUse(AcceptUse acceptUse, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<AcceptUse>(
            Type.ACCEPT_USE_REPLACED,
            Source.HANDLE_REPLACE_ACCEPT_USE,
            acceptUse)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_ACCEPTUSES")
    public void handleDeleteAllAcceptUses(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_ACCEPT_USES_DELETED,
            Source.HANDLE_DELETE_ALL_ACCEPT_USES,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_ACCEPTUSE")
    public void handleDeleteAcceptUse(ItemIdData acceptUseId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteAcceptUse: " + acceptUseId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.ACCEPT_USE_DELETED,
            Source.HANDLE_DELETE_ACCEPT_USE,
            acceptUseId)
            .fire();
    }

}