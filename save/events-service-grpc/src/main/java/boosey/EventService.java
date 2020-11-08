package boosey;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
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

    @Inject @RestClient KnativeEventServiceBroker knativeEventServiceBroker;
    private static MongoClient mongoClient;
    
    static Map<String, String> env = System.getenv();
    static String mongoUser = env.get("mongouser");
    static String mongoPwd = env.get("mongopwd");
    static String mongoPwdAdmin = env.get("mongopwdadmin");
    static String mongoHost = env.get("MONGO_EVENT_STORE_SERVICE_HOST");
    static String mongoPort = env.get("MONGO_EVENT_STORE_SERVICE_PORT");

    @ConfigProperty(name="boosey.default-spec-version")
    String defaultSpecVersion;

    private String defaultIfUnset(String value, String defaultValue) {
        return value.length() == 0 ? defaultValue : value;
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
                .setSubject(defaultIfUnset(evt.getSpecVersion(), defaultSpecVersion))
                .setEventData(evt.getEventData())
                .build();
    }

    @Override
    public Uni<FireReply> fire(FireRequest evtIn) {
        try {
            FireRequest evt = prepareRequest(evtIn);

            knativeEventServiceBroker.fire(
                evt.getEventId(), 
                evt.getType().name(), 
                evt.getSpecVersion(),
                evt.getSource().name(), 
                evt.getSubject(), 
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
