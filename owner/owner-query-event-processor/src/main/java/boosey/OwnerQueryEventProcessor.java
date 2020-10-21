package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class OwnerQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "OWNER_ADDED")
    @Transactional
    public void handleOwnerAdded(Owner owner, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        val r = new Owner(); 
        r.setId(owner.getId());
        r.setName(owner.getName());
        r.setEmail(owner.getEmail());
        r.setPhone(owner.getPhone());
        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "OWNER_REPLACED")
    @Transactional
    public void handleOwnerReplaced(Owner owner, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Owner r = Owner.findById(owner.getId());
        r.setName(owner.getName());
        r.setEmail(owner.getEmail());
        r.setPhone(owner.getPhone());
    }

    @Funq
    @CloudEventMapping(trigger = "ALL_OWNERS_DELETED")
    @Transactional
    public void handleAllOwnersDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Owner.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "OWNER_DELETED")
    @Transactional
    public void handleOwnerDeleted(ItemIdData ownerId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Owner.deleteById(ownerId.getId()); 
    }
}