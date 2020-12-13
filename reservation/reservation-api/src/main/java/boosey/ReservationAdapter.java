package boosey;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;
import lombok.extern.slf4j.Slf4j;
import boosey.ReservationCommon.ReservationGrpc;
// import boosey.ReservationCommon.State;
import boosey.ReservationCommon.ReservationGrpc.Builder;

@Slf4j
public class ReservationAdapter implements JsonbAdapter<ReservationGrpc, JsonObject> {  

    @Override
    public JsonObject adaptToJson(ReservationGrpc r) throws Exception {
        
        log.info("adaptToJson");

        JsonObjectBuilder b = Json.createObjectBuilder();

        if (r.getId() != null)
            b.add("id", r.getId());

        if (r.getResourceId() != null)            
            b.add("resourceId", r.getResourceId());

        if (r.getReserverId() != null)            
            b.add("reserverId", r.getReserverId());

        if (r.getStartTime() != null) {
            log.info("startTime: " + r.getStartTime());
            b.add("startTime", r.getStartTime());
        }

        if (r.getEndTime() != null) {
            log.info("endTime: " + r.getEndTime());
            b.add("endTime", r.getEndTime());
        }

        if (r.getState() != null)
            b.add("state", r.getState().name());

        return b.build();
    }

    private Boolean notNull(JsonObject r, String key) {

        return (r.containsKey(key)) && (r.get(key) != null);
    }

    @Override
    public ReservationGrpc adaptFromJson(JsonObject adapted) throws Exception {

        log.info("adapt From Json: " + adapted);

        Builder r = ReservationGrpc.newBuilder();
        log.info("created builder: " + r);

        if (notNull(adapted, "id"))
            r.setId(adapted.getString("id"));

        if (notNull(adapted, "resourceId"))
            r.setResourceId(adapted.getString("resourceId"));

        if (notNull(adapted, "reserverId"))
            r.setReserverId(adapted.getString("reserverId"));

        if (notNull(adapted, "startTime"))
            r.setStartTime(adapted.getString("startTime"));

        if (notNull(adapted, "endTime"))
            r.setEndTime(adapted.getString("endTime"));

        // if (notNull(adapted, "state"))
        //     r.setState(State.forNumber(adapted.getInt("state")));

        return r.build();
    }

}    
