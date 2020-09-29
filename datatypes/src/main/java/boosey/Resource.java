package boosey;

import javax.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Resource extends PanacheEntity {
    public Long id;
    public String eventRecordId;
    public String name;
    public String available;
}
