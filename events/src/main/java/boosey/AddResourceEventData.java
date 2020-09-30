package boosey;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddResourceEventData extends CommonEvent {

    @Getter(lazy=true) private final String resourceId = generateUUID();
    private String name;
    private String active;

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }    
}
