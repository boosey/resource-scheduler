package boosey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddResourceEventData {
    String name;
    Boolean available;
}
