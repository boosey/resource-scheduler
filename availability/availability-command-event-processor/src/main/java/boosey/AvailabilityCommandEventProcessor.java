package boosey;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.availability.Availability;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvailabilityCommandEventProcessor {
    
    @Inject @RestClient EventServiceClient events;
    @Inject @RestClient AvailabilityQueryClient query;
    @Inject @RestClient AvailabilityCommandClient command;

    @Funq
    @CloudEventMapping(trigger = "ADD_AVAILABILITY")
    public void handleAddAvailability(Availability availability, @Context CloudEvent eventContext) {

        events.fire(new ResourceSchedulerEvent<Availability>(
                    Type.AVAILABILITY_ADDED,
                    Source.HANDLE_ADD_AVAILABILITY,
                    availability));
    }    

    @Funq
    @CloudEventMapping(trigger = "REPLACE_AVAILABILITY")
    public void handleReplaceAvailability(Availability availability, @Context CloudEvent eventContext) {

         events.fire(new ResourceSchedulerEvent<Availability>(
                    Type.AVAILABILITY_REPLACED,
                    Source.HANDLE_REPLACE_AVAILABILITY,
                    availability));
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_AVAILABILITIES")
    public void handleDeleteAllAvailabilitys(NoEventData eventData, @Context CloudEvent eventContext) {

        log.info("handling delete all event");
        events.fire(new ResourceSchedulerEvent<String>(
            Type.ALL_AVAILABILITIES_DELETED,
            Source.HANDLE_DELETE_ALL_AVAILABILITIES,
            ""));
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_AVAILABILITY")
    public void handleDeleteAvailability(ItemIdData availabilityId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteAvailability: " + availabilityId);
        events.fire(new ResourceSchedulerEvent<ItemIdData>(
            Type.AVAILABILITY_DELETED,
            Source.HANDLE_DELETE_AVAILABILITY,
            availabilityId));
    }

    // HANDLE EXTERNAL EVENTS

    @Funq
    @CloudEventMapping(trigger = "RESOURCE_DELETED")
    public void handleResourceDeleted(ItemIdData resourceId, @Context CloudEvent evtCtx) {

        val r = query.availabilityForResource(resourceId.getId());

        if (r.getStatusInfo() == Status.OK) {
            @SuppressWarnings("unchecked")
            val l = (List<Availability>)r.getEntity();
            l.forEach(a -> {
                    command.deleteAvailability(a.getId());
            });
        }
    }    

    @Funq
    @CloudEventMapping(trigger = "ALL_RESOURCES_DELETED")
    public void handleAllResourcesDeleted(String nil, @Context CloudEvent evtCtx) {

        val r = query.listAll();
        if (r.getStatusInfo() == Status.OK) {
            @SuppressWarnings("unchecked")
            val l = (List<Availability>)r.getEntity();
            l.forEach(a -> {
                command.deleteAvailability(a.getId());
            });
        }
    }        

}
