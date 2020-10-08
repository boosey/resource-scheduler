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

        if (query.existsByName(resource.getName()).await().atMost(Duration.ofMillis(5000))) {
            throw new NotAcceptableException("Resource Exists");
        } else {
            new ResourceSchedulerEvent<Resource>(
                Type.ADD_RESOURCE,
                Source.RESOURCE_API,
                resource)
                .fire();
        }

        return resource.getResourceId();
    }    

    public Boolean deleteAllResources() {

        new ResourceSchedulerEvent<NoEventData>(
            Type.DELETE_ALL_RESOURCES,
            Source.RESOURCE_API,
            new NoEventData())
            .fire();

        return true;
    }    
}