package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.owner.Owner;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OwnerCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(OwnerCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_OWNER")
    public void handleAddOwner(Owner owner, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Owner>(
            Type.OWNER_ADDED,
            Source.HANDLE_ADD_OWNER,
            owner)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_OWNER")
    public void handleReplaceOwner(Owner owner, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Owner>(
            Type.OWNER_REPLACED,
            Source.HANDLE_REPLACE_OWNER,
            owner)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_OWNERS")
    public void handleDeleteAllOwners(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_OWNERS_DELETED,
            Source.HANDLE_DELETE_ALL_OWNERS,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_OWNER")
    public void handleDeleteOwner(ItemIdData ownerId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteOwner: " + ownerId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.OWNER_DELETED,
            Source.HANDLE_DELETE_OWNER,
            ownerId)
            .fire();
    }

}