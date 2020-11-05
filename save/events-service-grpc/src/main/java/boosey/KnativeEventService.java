package boosey;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;

@RegisterRestClient
@Singleton
public interface KnativeEventService {

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    String fire(
        @HeaderParam("Ce-Id") String eventId, 
        @HeaderParam("Ce-Type") String eventType, 
        @HeaderParam("Ce-Specversion") String eventSpecVersion, 
        @HeaderParam("Ce-Source") String eventSource,  
        @HeaderParam("Ce-Subject") String eventSubject,  
        @HeaderParam("Content-Type") String eventContentType,                 
        String eventData);
}