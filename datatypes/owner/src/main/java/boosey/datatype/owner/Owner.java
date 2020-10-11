package boosey.datatype.owner;

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
public class Owner extends PanacheEntityBase {
    @Id public String id = UUID.randomUUID().toString();
    public String name;
    public String phone;
    public String email;

    private static PanacheQuery<Owner> findByNameQuery(String name) {
        return Owner.find("LOWER(name) = ?1", name.toLowerCase());
    }

    public static Uni<Boolean> existsByName(String name){
        return new UniCreateWithEmitter<Boolean>(emitter -> {
            emitter.complete(findByNameQuery(name).count() > 0);
        });        
    }

    public static Uni<Owner> findByName(String name){
        return new UniCreateWithEmitter<Owner>(emitter -> {

            val OwnerQuery = findByNameQuery(name);
            if (OwnerQuery.count() > 0 ) {
                emitter.complete(OwnerQuery.firstResult());
            } else {
                emitter.fail(new NotFoundException());
            }            
        });           
    }
}
