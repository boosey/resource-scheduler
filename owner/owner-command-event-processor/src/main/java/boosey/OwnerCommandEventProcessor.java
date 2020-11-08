package boosey;

import javax.inject.Inject;
import com.google.gson.Gson;
import boosey.owner.Owner;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OwnerCommandEventProcessor {

    private static Gson gson = new Gson();

    @Inject
    @GrpcService("eventsservicegrpc")                   
    EventServiceGrpc.EventServiceBlockingStub grpcEvents;    

    @Funq
    @CloudEventMapping(trigger = "ADD_OWNER")
    public void handleAddOwner(Owner owner, @Context CloudEvent eventContext) {

        // val e = new ResourceSchedulerEvent<Owner>(
        //     Type.OWNER_ADDED,
        //     Source.HANDLE_ADD_OWNER,
        //     owner);

        // events.fire(e);
        
        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.OWNER_ADDED)
                        .setSource(EventSource.HANDLE_ADD_OWNER)
                        .setEventData(gson.toJson(owner))
                        .build());
      
    }

    @Funq
    @CloudEventMapping(trigger = "REPLACE_OWNER")
    public void handleReplaceOwner(Owner owner, @Context CloudEvent eventContext) {

        // val e = new ResourceSchedulerEvent<Owner>(
        //     Type.OWNER_REPLACED,
        //     Source.HANDLE_REPLACE_OWNER,
        //     owner);
            
        // events.fire(e);

        grpcEvents.fire(FireRequest.newBuilder()
                        .setType(EventType.OWNER_REPLACED)
                        .setSource(EventSource.HANDLE_REPLACE_OWNER)
                        .setEventData(gson.toJson(owner))
                        .build());        
    }
         
    @Funq
    @CloudEventMapping(trigger = "DELETE_ALL_OWNERS")
    public void handleDeleteAllOwners(NoEventData eventData, @Context CloudEvent eventContext) {

        // log.info("handling delete all event");
        // val e = new ResourceSchedulerEvent<NoEventData>(
        //     Type.ALL_OWNERS_DELETED,
        //     Source.HANDLE_DELETE_ALL_OWNERS,
        //     NoEventData.builder().build());
            
        // events.fire(e);
        grpcEvents.fire(FireRequest.newBuilder()
                .setType(EventType.ALL_OWNERS_DELETED)
                .setSource(EventSource.HANDLE_DELETE_ALL_OWNERS)
                .setEventData(gson.toJson(NoEventData.builder().build()))
                .build());
    }

    @Funq
    @CloudEventMapping(trigger = "DELETE_OWNER")
    public void handleDeleteOwner(ItemIdData ownerId, @Context CloudEvent evtCtx) {

        log.info("in command.handleDeleteOwner: " + ownerId);
        // val e = new ResourceSchedulerEvent<ItemIdData>(
        //     Type.OWNER_DELETED,
        //     Source.HANDLE_DELETE_OWNER,
        //     ownerId);
            
        // events.fire(e);

        grpcEvents.fire(FireRequest.newBuilder()
                .setType(EventType.OWNER_DELETED)
                .setSource(EventSource.HANDLE_DELETE_OWNER)
                .setEventData(gson.toJson(ownerId))
                .build());        
    }

}