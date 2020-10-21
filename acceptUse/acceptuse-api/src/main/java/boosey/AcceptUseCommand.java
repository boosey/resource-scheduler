package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.acceptuse.AcceptUse;
import io.smallrye.mutiny.Uni;

// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;

// @Slf4j
@ApplicationScoped
public class AcceptUseCommand {
    @Inject AcceptUseQuery query;

    public String addAcceptUse(AcceptUse acceptUse) {

        // TODO Need to check if the acceptUse slot already exists
        if (Uni.createFrom().item(true)
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("AcceptUse Exists");

        } else {
            ResourceSchedulerEvent.builder()
                .eventType(Type.ADD_OWNER)
                .source(Source.OWNER_API)
                .eventData(acceptUse)
                .build()
                .fire();                 
        }

        return acceptUse.getId();
    }    


    public Boolean replaceAcceptUse(String acceptUseId, AcceptUse acceptUse) {

        if (query.exists(acceptUseId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            acceptUse.setId(acceptUseId);

            ResourceSchedulerEvent.builder()
                .eventType(Type.REPLACE_OWNER)
                .source(Source.OWNER_API)
                .eventData(acceptUse)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("AcceptUse Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllAcceptUse() {

        ResourceSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_OWNERS)
            .source(Source.OWNER_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteAcceptUse(String acceptUseId) {

        if (query.exists(acceptUseId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            ResourceSchedulerEvent.builder()
                .eventType(Type.DELETE_OWNER)
                .source(Source.OWNER_API)
                .eventData(ItemIdData.builder().id(acceptUseId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("AcceptUse Does Not Exist");
        }

        return true;
    }    
}