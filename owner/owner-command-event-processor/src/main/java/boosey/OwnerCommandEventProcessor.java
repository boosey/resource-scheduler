package boosey;

import javax.inject.Inject;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OwnerCommandEventProcessor {

    @Inject
    @GrpcService("eventsservicegrpc")                   
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    

    @Funq
    @CloudEventMapping(trigger = "ADD_OWNER")
    public void handleAddOwner(EventData eventData, @Context CloudEvent eventContext) {
        log.info("responding to ADD_OWNER");
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.OWNER_ADDED)
                        .setSource(EventSource.HANDLE_ADD_OWNER)
                        .setEventData(eventData.value)
                        .build());
      
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_OWNER")
    public void handleReplaceOwner(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.OWNER_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_OWNER)
                        .setEventData(eventData.value)
                        .build());        
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_OWNERS")
    public void handleDeleteAllOwners(EventData eventData, @Context CloudEvent eventContext) {

        grpcEvents.fire(FireRequest.newBuilder()
                .setType(EventType.ALL_OWNERS_DELETED)
                .setSource(EventSource.HANDLE_DELETE_ALL_OWNERS)
                .setEventData(eventData.value)
                .build());
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_OWNER")
    public void handleDeleteOwner(EventData eventData, @Context CloudEvent evtCtx) {

        grpcEvents.fire(FireRequest.newBuilder()
                .setType(EventType.OWNER_DELETED)
                .setSource(EventSource.HANDLE_DELETE_OWNER)
                .setEventData(eventData.value)
                .build());        
    }

}