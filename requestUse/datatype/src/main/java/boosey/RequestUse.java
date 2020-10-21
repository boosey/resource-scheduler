package boosey;

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
public class RequestUse extends PanacheEntityBase {
    @Id public String id = UUID.randomUUID().toString();
    public String resourceId;
    public Boolean resourceActive;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    private static PanacheQuery<RequestUse> findByResourceIdQuery(String resourceId) {
        return RequestUse.find("resourceId = ?1", resourceId);
    }

    public static Uni<List<RequestUse>> findByResourceId(String resourceId){
        return new UniCreateWithEmitter<List<RequestUse>>(emitter -> {

            val requestUseQuery = findByResourceIdQuery(resourceId);
            emitter.complete(requestUseQuery.list());           
        });           
    }
}
