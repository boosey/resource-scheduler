package boosey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeleteResourceEventData extends CommonEvent {
    
    private String id;
 
}
