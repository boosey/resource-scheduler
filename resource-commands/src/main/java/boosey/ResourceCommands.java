package boosey;

import javax.inject.Inject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
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

    private Boolean isValid(AddResourceEventData e) {
        // check that there is no resource with the same name
        // return Uni<Boolean>.createFrom(true);
        return true;
    }

    @Funq
    @CloudEventMapping(trigger = "addResource", responseSource = "handleAddResource", responseType = "resourceAdded")
    public ResourceAddedEventData handleAddResource(AddResourceEventData evtData, @Context CloudEvent evtCtx) {

        InsertOneResult result;

        if (isValid(evtData)) {
            // Save event record to Mongo
            result = getCollection().insertOne(
                this.buildNewEventRecordFrom("addResource", evtCtx)
                        .append("_id", evtData.getEventId())
                        .append("eventData", evtData));
                        
            log.info("*** add resource *** separated evtRecord.id: " + result.getInsertedId());

            // Prepare new event to inform a Resource has been added
            val retEvt = new ResourceAddedEventData();
            retEvt.setInitiatingEventId(evtData.getEventId());
            retEvt.setResourceId(evtData.getResourceId());
            retEvt.setName(evtData.getName());
            retEvt.setActive(evtData.getActive());

            return retEvt;
        }

        return null;
    }
         
    @Funq
    @CloudEventMapping(trigger = "deleteAllResources", responseSource = "handleDeleteAllResources", responseType = "allResourcesDeleted")
    public AllResourcesDeletedEventData handleDeleteAllResources(DeleteAllResourcesEventData evtData, @Context CloudEvent evtCtx) {

        getCollection().insertOne(
            this.buildNewEventRecordFrom("deleteAllResources", evtCtx)
                .append("_id", evtData.getEventId()));

        val retEvt = new AllResourcesDeletedEventData();
        retEvt.setInitiatingEventId(evtData.getEventId());
        return retEvt;
    }

    @Funq
    @CloudEventMapping(trigger = "deleteResource", responseSource = "handleDeleteResource", responseType = "resourceDeleted")
    public ResourceDeletedEventData handleDeleteResource(DeleteResourceEventData evtData, @Context CloudEvent evtCtx) {

        getCollection().insertOne(
            this.buildNewEventRecordFrom("deleteResource", evtCtx)
                .append("_id", evtData.getEventId())
                .append("eventData", evtData));

        val retEvt = new ResourceDeletedEventData();
        retEvt.setInitiatingEventId(evtData.getEventId());
        retEvt.setId(evtData.getId());
        return retEvt;
    }

}