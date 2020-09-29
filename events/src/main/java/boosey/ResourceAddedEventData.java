package boosey;

import java.util.UUID;
import lombok.Data;
import lombok.Getter;

@Data
public class ResourceAddedEventData {

    @Getter(lazy=true) private final String eventId = generateUUID();
    private String initiatingEventId;
    private String resourceId;
    private String name;
    private String active;

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
