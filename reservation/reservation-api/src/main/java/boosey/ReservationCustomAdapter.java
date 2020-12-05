package boosey;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;

import io.quarkus.jsonb.JsonbConfigCustomizer;

@Singleton
public class ReservationCustomAdapter implements JsonbConfigCustomizer {

    public void customize(JsonbConfig config) {
        config.withAdapters(new ReservationAdapter());
    }

public class ReservationAdapter implements JsonbAdapter<ReservationGrpcQ, JsonObject> {
    @Override
    public JsonObject adaptToJson(ReservationGrpcQ r) throws Exception {
        return Json.createObjectBuilder()
            .add("id", r.getId())
            .add("resourceId", r.getResourceId())
            .add("reserverId", r.getReserverId())
            .add("startTime", r.getStartTime())
            .add("endTime", r.getEndTime())
            .add("state", r.getState())
            .build();
    }

    @Override
    public ReservationGrpcQ adaptFromJson(JsonObject adapted) throws Exception {
        ReservationGrpcQ r = ReservationGrpcQ.newBuilder()
            .setId(adapted.getString("id"))
            .setResourceId(adapted.getString("resourceId"))
            .setReserverId(adapted.getString("reserverId"))
            .setStartTime(adapted.getString("startTime"))
            .setEndTime(adapted.getString("endTime"))
            .setState(adapted.getString("state"))
            .build();
        return r;
    }

}    

}
