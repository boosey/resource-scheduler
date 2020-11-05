package boosey;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@Path("/events")
@RegisterRestClient(configKey="event-service")
public interface EventServiceClient {
    
    @POST
    @Path("/")
    public <T> String fire(ResourceSchedulerEvent<T> evt);    
}
