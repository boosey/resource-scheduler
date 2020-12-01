package boosey.resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Availability extends PanacheEntityBase {

    private static Jsonb jsonbResourceIdOnly = null;

    @Id public String id = UUID.randomUUID().toString();
    public String ownerName;
    public Boolean resourceActive = true;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name="resource_id", nullable=false)
    public Resource resource;       

    private static PanacheQuery<Availability> findByResourceIdQuery(String resourceId) {
        return Availability.find("resourceId = ?1", resourceId);
    }

    public static List<Availability> findByResourceId(String resourceId){
            val availabilityQuery = findByResourceIdQuery(resourceId);
            return availabilityQuery.list();                    
    }

    // Jsonb Utilities

    public static Jsonb jsonbResourceIdOnly() {

        if (Availability.jsonbResourceIdOnly == null) {
            JsonbConfig config = new JsonbConfig().withAdapters(new AvailabilityAdapterResourceIdOnly());
            Availability.jsonbResourceIdOnly = JsonbBuilder.create(config);  
        }

        return Availability.jsonbResourceIdOnly;
    }

    private static class AvailabilityAdapterResourceIdOnly implements JsonbAdapter<Availability, JsonObject> {
        @Override
        public JsonObject adaptToJson(Availability a) throws Exception {
            return Json.createObjectBuilder()
                .add("id", a.getId())
                .add("resource_id", a.getResource().getId())     
                .build();
        }

        @Override
        public Availability adaptFromJson(JsonObject adapted) throws Exception {
            Availability r = new Availability();
            r.setId(adapted.getString("id"));
            return r;
        }
    }        
}
