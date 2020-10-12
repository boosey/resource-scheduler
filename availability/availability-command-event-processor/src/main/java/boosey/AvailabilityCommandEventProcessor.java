package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.datatype.availability.Availability;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvailabilityCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(AvailabilityCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_OWNER")
    public void handleAddAvailability(Availability availability, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Availability>(
            Type.OWNER_ADDED,
            Source.HANDLE_ADD_OWNER,
            availability)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_OWNER")
    public void handleReplaceAvailability(Availability availability, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Availability>(
            Type.OWNER_REPLACED,
            Source.HANDLE_REPLACE_OWNER,
            availability)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_OWNERS")
    public void handleDeleteAllAvailabilitys(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_OWNERS_DELETED,
            Source.HANDLE_DELETE_ALL_OWNERS,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_OWNER")
    public void handleDeleteAvailability(ItemIdData availabilityId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteAvailability: " + availabilityId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.OWNER_DELETED,
            Source.HANDLE_DELETE_OWNER,
            availabilityId)
            .fire();
    }

}