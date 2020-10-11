package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.datatype.owner.Owner;

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
            ResourceSchedulerEvent.builder()
                .eventType(Type.ADD_OWNER)
                .source(Source.OWNER_API)
                .eventData(owner)
                .build()
                .fire();                 
        }

        return owner.getId();
    }    


    public Boolean replaceOwner(String ownerId, Owner owner) {

        if (query.exists(ownerId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            owner.setId(ownerId);

            ResourceSchedulerEvent.builder()
                .eventType(Type.REPLACE_OWNER)
                .source(Source.OWNER_API)
                .eventData(owner)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllOwners() {

        ResourceSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_OWNERS)
            .source(Source.OWNER_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteOwner(String ownerId) {

        if (query.exists(ownerId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            ResourceSchedulerEvent.builder()
                .eventType(Type.DELETE_OWNER)
                .source(Source.OWNER_API)
                .eventData(ItemIdData.builder().id(ownerId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }    
}