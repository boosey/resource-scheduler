package boosey.resource;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.ws.rs.NotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.UniCreateWithEmitter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
@Entity
public class Resource extends PanacheEntityBase {
    @Id public String id = UUID.randomUUID().toString();
    public String ownerId;
    public String name;
    public String description;
    public Boolean active = false;
    public Boolean availableByDefault = false;    

    private static PanacheQuery<Resource> findByNameQuery(String name) {
        return Resource.find("LOWER(name) = ?1", name.toLowerCase());
    }

    public static Uni<Boolean> existsByName(String name){
        return new UniCreateWithEmitter<Boolean>(emitter -> {
            emitter.complete(findByNameQuery(name).count() > 0);
        });        
    }

    public static Uni<Resource> findByName(String name){
        return new UniCreateWithEmitter<Resource>(emitter -> {

            val resourceQuery = findByNameQuery(name);
            if (resourceQuery.count() > 0 ) {
                emitter.complete(resourceQuery.firstResult());
            } else {
                emitter.fail(new NotFoundException());
            }            
        });           
    }
}
