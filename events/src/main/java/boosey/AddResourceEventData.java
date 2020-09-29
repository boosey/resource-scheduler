package boosey;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddResourceEventData {

    @Getter(lazy=true) private final String eventId = generateUUID();
    @Getter(lazy=true) private final String resourceId = generateUUID();
    private String name;
    private String active;

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }    
}
