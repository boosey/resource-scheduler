package boosey;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;
import com.google.gson.Gson;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import boosey.availability.Availability;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class AvailabilityCommandEventProcessor {

    private static Gson gson = new Gson();    

    @Inject
    @GrpcService("eventsservicegrpc")                     
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    
    
    @Inject @RestClient EventServiceClient events;
    @Inject @RestClient AvailabilityQueryClient query;
    @Inject @RestClient AvailabilityCommandClient command;

    @Funq
    @CloudEventMapping(trigger = "ADD_AVAILABILITY")
    public void handleAddAvailability(Availability availability, @Context CloudEvent eventContext) {

        // events.fire(new ResourceSchedulerEvent<Availability>(
        //             Type.AVAILABILITY_ADDED,
        //             Source.HANDLE_ADD_AVAILABILITY,
        //             availability));
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.AVAILABILITY_ADDED)
                        .setSource(EventSource.HANDLE_ADD_AVAILABILITY)
                        .setEventData(gson.toJson(availability))
                        .build());                      
    }    

    @Funq
    @CloudEventMapping(trigger = "REPLACE_AVAILABILITY")
    public void handleReplaceAvailability(Availability availability, @Context CloudEvent eventContext) {

        //  events.fire(new ResourceSchedulerEvent<Availability>(
        //             Type.AVAILABILITY_REPLACED,
        //             Source.HANDLE_REPLACE_AVAILABILITY,
        //             availability));
        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.AVAILABILITY_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_AVAILABILITY)
                        .setEventData(gson.toJson(availability))
                        .build());          
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_AVAILABILITIES")
    public void handleDeleteAllAvailabilitys(NoEventData eventData, @Context CloudEvent eventContext) {

        // log.info("handling delete all event");
        // events.fire(new ResourceSchedulerEvent<String>(
        //     Type.ALL_AVAILABILITIES_DELETED,
        //     Source.HANDLE_DELETE_ALL_AVAILABILITIES,
        //     ""));
        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.ALL_AVAILABILITIES_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_ALL_AVAILABILITIES)
                        .setEventData(gson.toJson(NoEventData.builder().build()))
                        .build());              
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_AVAILABILITY")
    public void handleDeleteAvailability(ItemIdData availabilityId, @Context CloudEvent evtCtx) {

        // log.info("in command.handleDeleteAvailability: " + availabilityId);
        // events.fire(new ResourceSchedulerEvent<ItemIdData>(
        //     Type.AVAILABILITY_DELETED,
        //     Source.HANDLE_DELETE_AVAILABILITY,
        //     availabilityId));
        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.AVAILABILITY_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_AVAILABILITY)
                        .setEventData(gson.toJson(availabilityId))
                        .build());               
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
