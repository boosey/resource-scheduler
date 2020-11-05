package boosey;

import java.util.Date;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import lombok.extern.slf4j.Slf4j;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Slf4j
@ApplicationScoped
@Path("/events")
public class EventService<T> {

    private static Gson gson = new Gson();
    @Inject @RestClient KnativeEventServiceBroker knativeEventService;
    private static MongoClient mongoClient;
    
    static Map<String, String> env = System.getenv();
    static String mongoUser = env.get("mongouser");
    static String mongoPwd = env.get("mongopwd");
    static String mongoPwdAdmin = env.get("mongopwdadmin");
    static String mongoHost = env.get("MONGO_EVENT_STORE_SERVICE_HOST");
    static String mongoPort = env.get("MONGO_EVENT_STORE_SERVICE_PORT");

    private static MongoClient getMongoClient() {
        if (EventService.mongoClient == null) {
            String connString =  new StringBuilder()
                .append("mongodb://")
                // .append(mongoUser)
                // .append(":")
                .append("admin:")
                // .append(mongoPwd)
                .append(mongoPwdAdmin)
                .append("@")
                .append(mongoHost)
                .append(":")
                .append(mongoPort)
                .toString();

            log.info(connString);
            EventService.mongoClient =
                MongoClients.create(connString);
        }
        return EventService.mongoClient;
    }

    String toJson() {
        log.info("Event JSON: " + gson.toJson(this));
        return gson.toJson(this);
    }

    private MongoCollection<Document> getCollection() {
        return EventService
            .getMongoClient()
            .getDatabase("resource_scheduler")
            .getCollection("events");
    }

    @POST
    @Path("/")
    public String fire(ResourceSchedulerEvent<T> evt) {

        knativeEventService.fire(
            evt.getEventId(), 
            evt.getEventType().name(), 
            evt.getSpecVersion(), 
            evt.getSource().name(), 
            evt.getSubject(), 
            MediaType.APPLICATION_JSON, 
            gson.toJson(evt.getEventData()));

        log.info("writing event data: " + gson.toJson(evt.getEventData()));

        this.getCollection().insertOne(new Document()
            .append("type", evt.getEventType().name())
            .append("source", evt.getSource().name())
            .append("specVersion", evt.getSpecVersion())
            .append("subject", evt.getSubject())
            .append("time", new Date())
            .append("eventData", gson.toJson(evt.getEventData())));

        log.info("returning from event fire");
        return evt.getEventId();
    }    
}
