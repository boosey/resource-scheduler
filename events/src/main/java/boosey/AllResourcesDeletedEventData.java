package boosey;

import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AllResourcesDeletedEventData {

    @Getter(lazy=true) private final String eventId = generateUUID();
    private String initiatingEventId;

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }    
}
