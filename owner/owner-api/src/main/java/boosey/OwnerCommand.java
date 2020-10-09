package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.OwnerSchedulerEvent.Source;
import boosey.OwnerSchedulerEvent.Type;
// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;

// @Slf4j
@ApplicationScoped
public class OwnerCommand {
    @Inject OwnerQuery query;

    public String addOwner(Owner owner) {

        if (query.existsByName(owner.getName())
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("Owner Exists");

        } else {
            OwnerSchedulerEvent.builder()
                .eventType(Type.ADD_RESOURCE)
                .source(Source.RESOURCE_API)
                .eventData(owner)
                .build()
                .fire();                 
        }

        return owner.getOwnerId();
    }    


    public Boolean replaceOwner(String ownerId, Owner owner) {

        if (query.exists(ownerId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            owner.setOwnerId(ownerId);

            OwnerSchedulerEvent.builder()
                .eventType(Type.REPLACE_RESOURCE)
                .source(Source.RESOURCE_API)
                .eventData(owner)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllOwners() {

        OwnerSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_RESOURCES)
            .source(Source.RESOURCE_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteOwner(String ownerId) {

        if (query.exists(ownerId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            OwnerSchedulerEvent.builder()
                .eventType(Type.DELETE_RESOURCE)
                .source(Source.RESOURCE_API)
                .eventData(ItemIdData.builder().id(ownerId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }    
}