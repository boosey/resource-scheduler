package boosey;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.MediaType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class EventService extends MutinyEventServiceGrpc.EventServiceImplBase {

    private static Jsonb jsonb = JsonbBuilder.create();

    @Inject @RestClient KnativeEventServiceBroker knativeEventServiceBroker;
    private static MongoClient mongoClient;
    
    static Map<String, String> env = System.getenv();
    // static String mongoUser = env.get("MONGODB_USER");
    // static String mongoPwd = env.get("MONGODB_PASSWORD");
    static String mongoPwdAdmin = env.get("MONGODB_ADMIN_PASSWORD");
    static String mongoHost = env.get("EVENTS_DATA_SERVICE_HOST");
    static String mongoPort = env.get("EVENTS_DATA_SERVICE_PORT");

    @ConfigProperty(name="boosey.default-spec-version")
    String defaultSpecVersion;

    private String defaultIfUnset(String value, String defaultValue) {
        return (value == null || value.length() == 0) ? defaultValue : value;
    }

    private String defaultToUUIDIfUnset(String value) {
         return defaultIfUnset(value, UUID.randomUUID().toString());
    }

    private FireRequest prepareRequest(FireRequest evt) {
        return FireRequest.newBuilder()
                .setEventId(defaultToUUIDIfUnset(evt.getEventId()))
                .setType(evt.getType())
                .setSpecVersion(defaultIfUnset(evt.getSpecVersion(), defaultSpecVersion))
                .setSource(evt.getSource())
                // .setSubject(defaultIfUnset(evt.getSpecVersion(), "null"))
                .setEventData(jsonb.toJson(EventData.builder().value(evt.getEventData()).build()))
                .build();
    }

    @Override
    public Uni<FireReply> fire(FireRequest evtIn) {
        try {

            FireRequest evt = prepareRequest(evtIn);
            log.info("------------------------------------------------------------------------------");
            log.info("eventId: " + evt.getEventId());
            log.info("event type / source: " + evt.getType().name() + " / " + evt.getSource().name());
            log.info("spec version: " + evt.getSpecVersion());
            log.info("media type: " + MediaType.APPLICATION_JSON);
            log.info("data: " + evt.getEventData());
            log.info("------------------------------------------------------------------------------");

            knativeEventServiceBroker.fire(
                evt.getEventId(), 
                evt.getType().name(), 
                evt.getSpecVersion(),
                evt.getSource().name(), 
                // evt.getSubject(), 
                MediaType.APPLICATION_JSON, 
                evt.getEventData());
         
            this.getCollection().insertOne(new Document()
                .append("type", evt.getType().name())
                .append("source", evt.getSource().name())
                .append("specVersion", evt.getSpecVersion())
                .append("subject", evt.getSubject())
                .append("time", new Date())
                .append("eventData", evt.getEventData()));  

            return Uni.createFrom().item(
                FireReply.newBuilder().setEventId(evt.getEventId()).build());
                
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }    

    private static MongoClient getMongoClient() {
        if (EventService.mongoClient == null) {
            log.info("adminpwd: " + mongoPwdAdmin);
            String connString =  new StringBuilder()
                .append("mongodb://")
                .append("admin:")
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

    private MongoCollection<Document> getCollection() {
        return EventService
            .getMongoClient()
            .getDatabase("resource_scheduler")
            .getCollection("events");
    }
}
