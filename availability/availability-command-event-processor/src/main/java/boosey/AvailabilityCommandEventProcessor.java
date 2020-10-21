package boosey;

import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.availability.Availability;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvailabilityCommandEventProcessor {

    // private static final Logger log = Logger.getLogger(AvailabilityCommandEventProcessor.class);

    @Funq
    @CloudEventMapping(trigger = "ADD_AVAILABILITY")
    public void handleAddAvailability(Availability availability, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Availability>(
            Type.AVAILABILITY_ADDED,
            Source.HANDLE_ADD_AVAILABILITY,
            availability)
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_AVAILABILITY")
    public void handleReplaceAvailability(Availability availability, @Context CloudEvent eventContext) {

        new ResourceSchedulerEvent<Availability>(
            Type.AVAILABILITY_REPLACED,
            Source.HANDLE_REPLACE_AVAILABILITY,
            availability)
            .fire();
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_AVAILABILITYS")
    public void handleDeleteAllAvailabilitys(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        new ResourceSchedulerEvent<String>(
            Type.ALL_AVAILABILITIES_DELETED,
            Source.HANDLE_DELETE_ALL_AVAILABILITIES,
            "")
            .fire();
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_AVAILABILITY")
    public void handleDeleteAvailability(ItemIdData availabilityId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteAvailability: " + availabilityId);
        new ResourceSchedulerEvent<ItemIdData>(
            Type.AVAILABILITY_DELETED,
            Source.HANDLE_DELETE_AVAILABILITY,
            availabilityId)
            .fire();
    }

}