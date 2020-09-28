package boosey;

import org.jboss.logging.Logger;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.val;
import io.quarkus.funqy.knative.events.CloudEvent;

@MongoEntity
public class ResourceCommandsEventRecord extends PanacheMongoEntity {
    private static final Logger log = Logger.getLogger(ResourceCommands.class);

    public String eventId;
    public String specVersion;
    public String source;
    public String subject;
    public String time;

    public AddResourceEventData addResourceData;

    public static ResourceCommandsEventRecord buildWith(final CloudEvent evtCtx, final AddResourceEventData evtData) {
        val evtRec = new ResourceCommandsEventRecord();

        log.info("*** emit event *** evtData: " + evtData);
        log.info("*** emit event *** evtCtx: " + evtCtx);
        evtRec.createBaseRecord(evtCtx).addResourceData = evtData;
        return evtRec;
    }

    private ResourceCommandsEventRecord createBaseRecord(CloudEvent evtCtx) {
           
        eventId = evtCtx.id();
        specVersion = evtCtx.specVersion();
        source = evtCtx.source();
        subject = evtCtx.subject();
        time = evtCtx.time().toString();

        return this;
    }

}
