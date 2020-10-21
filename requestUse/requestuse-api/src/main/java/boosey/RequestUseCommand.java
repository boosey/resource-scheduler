package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import io.smallrye.mutiny.Uni;

// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;

// @Slf4j
@ApplicationScoped
public class RequestUseCommand {
    @Inject RequestUseQuery query;

    public String addRequestUse(RequestUse requestUse) {

        // TODO Need to check if the requestUse slot already exists
        if (Uni.createFrom().item(true)
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("RequestUse Exists");

        } else {
            ResourceSchedulerEvent.builder()
                .eventType(Type.ADD_OWNER)
                .source(Source.OWNER_API)
                .eventData(requestUse)
                .build()
                .fire();                 
        }

        return requestUse.getId();
    }    


    public Boolean replaceRequestUse(String requestUseId, RequestUse requestUse) {

        if (query.exists(requestUseId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            requestUse.setId(requestUseId);

            ResourceSchedulerEvent.builder()
                .eventType(Type.REPLACE_OWNER)
                .source(Source.OWNER_API)
                .eventData(requestUse)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("RequestUse Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllRequestUse() {

        ResourceSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_OWNERS)
            .source(Source.OWNER_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteRequestUse(String requestUseId) {

        if (query.exists(requestUseId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            ResourceSchedulerEvent.builder()
                .eventType(Type.DELETE_OWNER)
                .source(Source.OWNER_API)
                .eventData(ItemIdData.builder().id(requestUseId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("RequestUse Does Not Exist");
        }

        return true;
    }    
}