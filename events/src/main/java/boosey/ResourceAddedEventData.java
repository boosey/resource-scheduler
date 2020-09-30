package boosey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResourceAddedEventData extends CommonEvent  {

    private String initiatingEventId;
    private String resourceId;
    private String name;
    private String active;

}
