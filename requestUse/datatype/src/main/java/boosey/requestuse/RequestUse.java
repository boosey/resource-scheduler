package boosey.requestuse;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
@Entity
public class RequestUse extends PanacheEntityBase {
    @Id public String id = UUID.randomUUID().toString();
    public String requesterId;
    public String resourceId;
    public String availabilityId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
}
