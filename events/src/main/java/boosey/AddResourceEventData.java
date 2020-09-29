package boosey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class AddResourceEventData {
    @Getter @Setter String name;
    @Getter @Setter String available;
}
