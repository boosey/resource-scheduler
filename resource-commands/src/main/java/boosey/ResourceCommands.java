package boosey;

import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.jboss.logging.Logger;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;

public class ResourceCommands {
    private static final Logger log = Logger.getLogger(ResourceCommands.class);

    @Inject MongoClient mongoClient;
    
    Document buildNewEventRecordFrom(String eventType, CloudEvent evtCtx) {

        return new Document()
            .append("type", eventType)
            .append("source", evtCtx.source())
            .append("specVersion", evtCtx.specVersion())
            .append("subject", evtCtx.subject())
            .append("time", evtCtx.time().toString());
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("resources").getCollection("events");
    }

    @Funq
    @CloudEventMapping(trigger = "addResource", responseSource = "handleAddResource", responseType = "resourceAdded")
    public ResourceAddedEventData handleAddResource(AddResourceEventData evtData, @Context CloudEvent evtCtx) {

        val result = getCollection().insertOne(
            this.buildNewEventRecordFrom("addResource", evtCtx)
                    .append("eventData", evtData));

        log.info("*** add resource *** separated evtRecord.id: " + result.getInsertedId() );

        val retEvt = new ResourceAddedEventData();
        retEvt.recordId = result.getInsertedId().toString();
        retEvt.name = evtData.name;
        retEvt.available = evtData.available;

        return retEvt;
    }


}