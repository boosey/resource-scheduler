package boosey.resource;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.ws.rs.NotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Resource extends PanacheEntityBase {

    private static Jsonb jsonbNoAvailability = null;

    @Id public String id = UUID.randomUUID().toString();
    public String ownerId;
    public String name;
    public String description;
    public Boolean active = false;
    public Boolean availableByDefault = false;    

    @OneToMany(mappedBy="resource", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)  
    public List<Availability> availabilities = new ArrayList<>(); 

    public void addAvailability(Availability a) {
        this.getAvailabilities().add(a);
        a.setResource(this);
    }
    public void removeAvailability(Availability a) {
        this.getAvailabilities().remove(a);
        a.setResource(null);
    }

    private static PanacheQuery<Resource> findByNameQuery(String name) {
        log.info("find by name: " + name);
        return Resource.find("LOWER(name) = ?1", name.toLowerCase());
    }

    public static Boolean existsByName(String name){
        log.info("exists by name: " + name);
        log.info("count: " + findByNameQuery(name).count());
        
        return findByNameQuery(name).count() > 0;   
    }

    public static Resource findByName(String name){

            val resourceQuery = findByNameQuery(name);
            if (resourceQuery.count() > 0 ) {
                return resourceQuery.firstResult();
            } else {
                throw new NotFoundException();
            }                      
    }

    public static Jsonb jsonbNoAvailability() {

        if (Resource.jsonbNoAvailability == null) {
            JsonbConfig config = new JsonbConfig().withAdapters(new ResourceAdapterNoAvailability());
            Resource.jsonbNoAvailability = JsonbBuilder.create(config);  
        }

        return Resource.jsonbNoAvailability;
    }

    private static class ResourceAdapterNoAvailability implements JsonbAdapter<Resource, JsonObject> {
        @Override
        public JsonObject adaptToJson(Resource r) throws Exception {
            JsonObjectBuilder builder =  Json.createObjectBuilder();
            builder.add("id", r.getId());
            ResourceUtil.addIfSet(builder, "name", r.getName());
            ResourceUtil.addIfSet(builder, "description", r.getDescription());
            ResourceUtil.addIfSet(builder, "ownerId", r.getOwnerId());
            ResourceUtil.addIfSet(builder, "active", r.getActive());
            ResourceUtil.addIfSet(builder, "availableByDefault", r.getAvailableByDefault());

            return builder.build();
        }

        @Override
        public Resource adaptFromJson(JsonObject adapted) throws Exception {
            Resource r = new Resource();
            r.setId(adapted.getString("id"));
            return r;
        }
    }            
}
