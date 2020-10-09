package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;

// @Slf4j
@ApplicationScoped
public class ResourceCommand {
    @Inject ResourceQuery query;

    public String addResource(Resource resource) {

        if (query.existsByName(resource.getName())
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("Resource Exists");

        } else {
            ResourceSchedulerEvent.builder()
                .eventType(Type.ADD_RESOURCE)
                .source(Source.RESOURCE_API)
                .eventData(resource)
                .build()
                .fire();                 
        }

        return resource.getResourceId();
    }    


    public Boolean replaceResource(String resourceId, Resource resource) {

        if (query.exists(resourceId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            resource.setResourceId(resourceId);

            ResourceSchedulerEvent.builder()
                .eventType(Type.REPLACE_RESOURCE)
                .source(Source.RESOURCE_API)
                .eventData(resource)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Resource Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllResources() {

        ResourceSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_RESOURCES)
            .source(Source.RESOURCE_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteResource(String resourceId) {

        if (query.exists(resourceId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            ResourceSchedulerEvent.builder()
                .eventType(Type.DELETE_RESOURCE)
                .source(Source.RESOURCE_API)
                .eventData(ItemIdData.builder().id(resourceId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Resource Does Not Exist");
        }

        return true;
    }    
}