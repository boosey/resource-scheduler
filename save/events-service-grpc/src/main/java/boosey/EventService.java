package boosey;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class EventService extends MutinyEventServiceGrpc.EventServiceImplBase {

    @Inject @RestClient KnativeEventService knativeEventService;
    private static MongoClient mongoClient;    

    static Map<String, String> env = System.getenv();
    static String mongoUser = env.get("mongouser");
    static String mongoPwd = env.get("mongopwd");
    static String mongoPwdAdmin = env.get("mongopwdadmin");
    static String mongoHost = env.get("MONGO_EVENT_STORE_SERVICE_HOST");
    static String mongoPort = env.get("MONGO_EVENT_STORE_SERVICE_PORT");;

    @Override
    public Uni<FireReply> fire(FireRequest request) {

        knativeEventService.fire(
            request.getEventId(), 
            request.getType().name(),
            request.getSpecVersion(), 
            request.getSource().name(), 
            request.getSubject(), 
            MediaType.APPLICATION_JSON, 
            request.getEventData());

        log.info("writing event data: " + gson.toJson(request.getEventData()));

        this.getCollection().insertOne(new Document()
            .append("type", request.getType().name())
            .append("source", request.getSource().name())
            .append("specVersion", request.getSpecVersion())
            .append("subject", request.getSubject())
            .append("time", new Date())
            .append("eventData", gson.toJson(request.getEventData())));

        log.info("returning from event fire");     

        return Uni.createFrom().item(
            FireReply.newBuilder()
                .setEventId(request.getEventId())
                .build()  
        );
    }   

    private MongoCollection<Document> getCollection() {
        return EventService
            .getMongoClient()
            .getDatabase("resource_scheduler")
            .getCollection("events");
    }

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

    // private static KnativeEventService getKnativeEventService() {
    //     if (knativeEventService == null) {
    //         EventService.knativeEventService = 
    //             RestClientBuilder.newBuilder()
    //                 .baseUri(URI.create("http://default-broker.resource-scheduler.svc.cluster.local"))
    //                 .build(KnativeEventService.class); 
    //     }
    //     return EventService.knativeEventService;
    // } 

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    } 

    private static Gson gson = new Gson();    

}
