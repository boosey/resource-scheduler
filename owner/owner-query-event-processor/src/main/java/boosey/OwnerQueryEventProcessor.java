package boosey;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import boosey.owner.Owner;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class OwnerQueryEventProcessor {

    private static Jsonb jsonb = JsonbBuilder.create();        

    @Funq
    @CloudEventMapping(trigger = "OWNER_ADDED")
    @Transactional
    public void handleOwnerAdded(EventData eventData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Owner owner = jsonb.fromJson(eventData.value, Owner.class);

        val r = new Owner(); 
        r.setId(owner.getId());
        r.setName(owner.getName());
        r.setEmail(owner.getEmail());
        r.setPhone(owner.getPhone());
        r.setActive(owner.getActive());
        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "OWNER_REPLACED")
    @Transactional
    public void handleOwnerReplaced(EventData eventData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Owner owner = jsonb.fromJson(eventData.value, Owner.class);

        Owner r = Owner.findById(owner.getId());
        r.setName(owner.getName());
        r.setEmail(owner.getEmail());
        r.setPhone(owner.getPhone());
        r.setActive(owner.getActive());
    }

    @Funq
    @CloudEventMapping(trigger = "ALL_OWNERS_DELETED")
    @Transactional
    public void handleAllOwnersDeleted(EventData nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Owner.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "OWNER_DELETED")
    @Transactional
    public void handleOwnerDeleted(EventData eventData, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        ItemIdData ownerId = jsonb.fromJson(eventData.value, ItemIdData.class);

        Owner.deleteById(ownerId.getId()); 
    }
}