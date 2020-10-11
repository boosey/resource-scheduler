package boosey;

import java.net.URI;
import java.util.Date;
import java.util.UUID;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Getter
@ToString
public class ResourceSchedulerEvent<T> {

    public enum Type {
        ADD_RESOURCE,
        RESOURCE_ADDED,
        REPLACE_RESOURCE,
        RESOURCE_REPLACED,
        DELETE_ALL_RESOURCES,
        ALL_RESOURCES_DELETED,
        DELETE_RESOURCE,
        RESOURCE_DELETED,
        RESOURCE_COMMAND_COMPLETE,
        ADD_OWNER,
        OWNER_ADDED,
        REPLACE_OWNER,
        OWNER_REPLACED,
        DELETE_ALL_OWNERS,
        ALL_OWNERS_DELETED,
        DELETE_OWNER,
        OWNER_DELETED,
        OWNER_COMMAND_COMPLETE      
    }

    public enum Source {
        RESOURCE_API,
        HANDLE_ADD_RESOURCE,
        HANDLE_REPLACE_RESOURCE,
        HANDLE_DELETE_ALL_RESOURCES,
        HANDLE_DELETE_RESOURCE,
        OWNER_API,
        HANDLE_ADD_OWNER,
        HANDLE_REPLACE_OWNER,
        HANDLE_DELETE_ALL_OWNERS,
        HANDLE_DELETE_OWNER   
    }

    private static Gson gson = new Gson();
    private static EventService eventService;
    private static MongoClient mongoClient;
    
    private final String eventId = generateUUID();
    private final Type eventType;    
    private final Source source;
    private final String specVersion = "1.0";
    private final String subject;
    private final T eventData;

    private static EventService getEventService() {
        if (ResourceSchedulerEvent.eventService == null) {
            ResourceSchedulerEvent.eventService = 
                RestClientBuilder.newBuilder()
                    .baseUri(URI.create("http://default-broker.resource-scheduler.svc.cluster.local"))
                    .build(EventService.class); 
        }
        return ResourceSchedulerEvent.eventService;
    }

    private static MongoClient getMongoClient() {
        if (ResourceSchedulerEvent.mongoClient == null) {
            ResourceSchedulerEvent.mongoClient =
                MongoClients.create("mongodb://resource-commands-events-data.resource-scheduler.svc.cluster.local:27017");
        }
        return ResourceSchedulerEvent.mongoClient;
    }

    public ResourceSchedulerEvent(Type eventType, Source source, T eventData) {
        this.eventType = eventType;
        this.source = source;
        this.subject = "";
        this.eventData = eventData;
    }

    public ResourceSchedulerEvent(Type eventType, Source source, String subject, T eventData) {
        this.eventType = eventType;
        this.source = source;
        this.subject = subject;
        this.eventData = eventData;
    }

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    } 

    String toJson() {
        log.info("Event JSON: " + gson.toJson(this));
        return gson.toJson(this);
    }

    private MongoCollection<Document> getCollection() {
        return ResourceSchedulerEvent.getMongoClient().getDatabase("resources").getCollection("events");
    }

    public String fire() {

        ResourceSchedulerEvent.getEventService().fire(
            this.getEventId(), 
            this.getEventType().name(), 
            this.getSpecVersion(), 
            this.getSource().name(), 
            this.getSubject(), 
            MediaType.APPLICATION_JSON, 
            gson.toJson(this.getEventData()));
            // this.toJson());  // MAYBE NEEDS TO BE ToJson()  

        log.info("writing event data: " + gson.toJson(this.getEventData()));

        this.getCollection().insertOne(new Document()
            .append("type", this.getEventType().name())
            .append("source", this.getSource().name())
            .append("specVersion", this.getSpecVersion())
            .append("subject", this.getSubject())
            .append("time", new Date())
            .append("eventData", gson.toJson(this.getEventData())));

        log.info("returning from event fire");
        return this.getEventId();
    }
}
