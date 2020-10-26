package boosey.availability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
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
public class Availability extends PanacheEntityBase {
    @Id public String id = UUID.randomUUID().toString();
    public String resourceId;
    public String resourceName;
    public String ownerName;
    public Boolean resourceActive = true;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    private static PanacheQuery<Availability> findByResourceIdQuery(String resourceId) {
        return Availability.find("resourceId = ?1", resourceId);
    }

    public static Uni<List<Availability>> findByResourceId(String resourceId){
        return new UniCreateWithEmitter<List<Availability>>(emitter -> {

            val availabilityQuery = findByResourceIdQuery(resourceId);
            emitter.complete(availabilityQuery.list());           
        });           
    }
}
