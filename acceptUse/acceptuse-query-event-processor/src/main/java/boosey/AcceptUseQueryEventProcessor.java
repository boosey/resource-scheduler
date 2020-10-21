package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;

import boosey.acceptuse.AcceptUse;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class AcceptUseQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "ACCEPT_USE_ADDED")
    @Transactional
    public void handleAcceptUseAdded(AcceptUse acceptUse, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        val r = new AcceptUse(); 
        r.setId(acceptUse.getId());

        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "ACCEPT_USE_REPLACED")
    @Transactional
    public void handleAcceptUseReplaced(AcceptUse acceptUse, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        // AcceptUse r = AcceptUse.findById(acceptUse.getId());

    }

    @Funq
    @CloudEventMapping(trigger = "ALL_ACCEPT_USES_DELETED")
    @Transactional
    public void handleAllAcceptUsesDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        AcceptUse.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "ACCEPT_USE_DELETED")
    @Transactional
    public void handleAcceptUseDeleted(ItemIdData acceptUseId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        AcceptUse.deleteById(acceptUseId.getId()); 
    }
}