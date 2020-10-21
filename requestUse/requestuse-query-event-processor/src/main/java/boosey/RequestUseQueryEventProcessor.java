package boosey;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;

import boosey.requestuse.RequestUse;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import lombok.val;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class RequestUseQueryEventProcessor {

    @Funq
    @CloudEventMapping(trigger = "REQUEST_USE_ADDED")
    @Transactional
    public void handleRequestUseAdded(RequestUse requestUse, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        val r = new RequestUse(); 
        r.setId(requestUse.getId());

        r.persist();
    }

    @Funq
    @CloudEventMapping(trigger = "REQUEST_USE_REPLACED")
    @Transactional
    public void handleRequestUseReplaced(RequestUse requestUse, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        // TODO
        // RequestUse r = RequestUse.findById(requestUse.getId());

    }

    @Funq
    @CloudEventMapping(trigger = "ALL_REQUEST_USES_DELETED")
    @Transactional
    public void handleAllRequestUsesDeleted(String nil, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        RequestUse.deleteAll();
    }

    @Funq
    @CloudEventMapping(trigger = "REQUEST_USE_DELETED")
    @Transactional
    public void handleRequestUseDeleted(ItemIdData requestUseId, @Context CloudEvent evtCtx) throws SecurityException, IllegalStateException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        RequestUse.deleteById(requestUseId.getId()); 
    }
}