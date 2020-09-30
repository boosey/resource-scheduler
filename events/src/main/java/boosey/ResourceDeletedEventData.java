package boosey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResourceDeletedEventData  extends CommonEvent {

    private String initiatingEventId;
    private String id;
 
}
