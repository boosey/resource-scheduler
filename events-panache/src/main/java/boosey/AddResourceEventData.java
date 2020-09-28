package boosey;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class AddResourceEventData extends PanacheMongoEntity {
    @Getter @Setter String name;
    @Getter @Setter String available;
}
