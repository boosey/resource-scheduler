package boosey;

import javax.inject.Inject;
// import javax.json.bind.Jsonb;
// import javax.json.bind.JsonbBuilder;

// import boosey.resource.Availability;
// import boosey.resource.Resource;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceCommandEventProcessor {   

    @Inject
    @GrpcService("eventsservicegrpc")                     
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    
    
    @Funq
    @CloudEventMapping(trigger = "ADD_RESOURCE")
    public void handleAddResource(EventData eventData, @Context CloudEvent eventContext) {

        log.info("incoming string: " + eventData);
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.RESOURCE_ADDED)
                        .setSource(EventSource.HANDLE_ADD_RESOURCE)
                        .setEventData(eventData.value)
                        .build());                      
    }    

    @Funq
    @CloudEventMapping(trigger = "REPLACE_RESOURCE")
    public void handleReplaceResource(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.RESOURCE_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_RESOURCE)
                        .setEventData(eventData.value)
                        .build());          
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_RESOURCES")
    public void handleDeleteAllResources(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.ALL_RESOURCES_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_ALL_RESOURCES)
                        .setEventData(eventData.value)
                        .build());              
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_RESOURCE")
    public void handleDeleteResource(EventData eventData, @Context CloudEvent evtCtx) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.RESOURCE_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_RESOURCE)
                        .setEventData(eventData.value)
                        .build());               
    }

    // Availability Events

    @Funq
    @CloudEventMapping(trigger = "ADD_AVAILABILITY")
    public void handleAddAvailability(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.AVAILABILITY_ADDED)
                        .setSource(EventSource.HANDLE_ADD_AVAILABILITY)
                        .setEventData(eventData.value)
                        .build());                      
    }    

    @Funq
    @CloudEventMapping(trigger = "REPLACE_AVAILABILITY")
    public void handleReplaceAvailability(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.AVAILABILITY_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_AVAILABILITY)
                        .setEventData(eventData.value)
                        .build());          
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_AVAILABILITIES")
    public void handleDeleteAllAvailabilitys(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.ALL_AVAILABILITIES_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_ALL_AVAILABILITIES)
                        .setEventData(eventData.value)
                        .build());              
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_AVAILABILITY")
    public void handleDeleteAvailability(EventData eventData, @Context CloudEvent evtCtx) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.AVAILABILITY_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_AVAILABILITY)
                        .setEventData(eventData.value)
                        .build());               
    }
}
