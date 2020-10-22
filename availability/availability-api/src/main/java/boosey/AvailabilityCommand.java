package boosey;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;

// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;

// @Slf4j
@ApplicationScoped
public class AvailabilityCommand {
    @Inject AvailabilityQuery query;

    public String addAvailability(Availability availability) {

        // TODO Need to check if the availability slot already exists
        if (Uni.createFrom().item(true)
                .await().atMost(Duration.ofMillis(5000))) {

            throw new NotAcceptableException("Availability Exists");

        } else {
            ResourceSchedulerEvent.builder()
                .eventType(Type.ADD_AVAILABILITY)
                .source(Source.AVAILABILITY_API)
                .eventData(availability)
                .build()
                .fire();                 
        }

        return availability.getId();
    }    


    public Boolean replaceAvailability(String availabilityId, Availability availability) {

        if (query.exists(availabilityId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            // Just in case
            availability.setId(availabilityId);

            ResourceSchedulerEvent.builder()
                .eventType(Type.REPLACE_AVAILABILITY)
                .source(Source.AVAILABILITY_API)
                .eventData(availability)
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Availability Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllAvailability() {

        ResourceSchedulerEvent.builder()
            .eventType(Type.DELETE_ALL_AVAILABILITYS)
            .source(Source.AVAILABILITY_API)
            .eventData(new NoEventData())
            .build()
            .fire();

        return true;
    }    

    public Boolean deleteAvailability(String availabilityId) {

        if (query.exists(availabilityId)
                    .await().atMost(Duration.ofMillis(5000)).booleanValue()) {
    
            ResourceSchedulerEvent.builder()
                .eventType(Type.DELETE_AVAILABILITY)
                .source(Source.AVAILABILITY_API)
                .eventData(ItemIdData.builder().id(availabilityId).build())
                .build()
                .fire();    

        } else {        
            throw new NotAcceptableException("Availability Does Not Exist");
        }

        return true;
    }    
}