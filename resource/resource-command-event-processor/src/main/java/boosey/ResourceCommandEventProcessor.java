package boosey;

import javax.inject.Inject;
import com.google.gson.Gson;
import boosey.resource.Resource;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.grpc.runtime.annotations.GrpcService;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class ResourceCommandEventProcessor {

    private static Gson gson = new Gson();    

    @Inject
    @GrpcService("eventsservicegrpc")                     
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    
    
    @Funq
    @CloudEventMapping(trigger = "ADD_RESOURCE")
    public void handleAddResource(Resource resource, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.RESOURCE_ADDED)
                        .setSource(EventSource.HANDLE_ADD_RESOURCE)
                        .setEventData(gson.toJson(resource))
                        .build());                      
    }    

    @Funq
    @CloudEventMapping(trigger = "REPLACE_RESOURCE")
    public void handleReplaceResource(Resource resource, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.RESOURCE_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_RESOURCE)
                        .setEventData(gson.toJson(resource))
                        .build());          
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_RESOURCES")
    public void handleDeleteAllResources(NoEventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.ALL_AVAILABILITIES_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_ALL_AVAILABILITIES)
                        .setEventData(gson.toJson(NoEventData.builder().build()))
                        .build());              
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_RESOURCE")
    public void handleDeleteResource(ItemIdData resourceId, @Context CloudEvent evtCtx) {

        grpcEvents.fire(FireRequest.newBuilder()        
                        .setType(EventType.RESOURCE_DELETED)
                        .setSource(EventSource.HANDLE_DELETE_RESOURCE)
                        .setEventData(gson.toJson(resourceId))
                        .build());               
    }
}
