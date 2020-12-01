package boosey.resource;

import javax.json.JsonObjectBuilder;

public class ResourceUtil {
    

    public static JsonObjectBuilder addIfSet(JsonObjectBuilder builder, String key, String value) {
        if (value != null)
            builder.add(key, value);

        return builder;
    }

    public static JsonObjectBuilder addIfSet(JsonObjectBuilder builder, String key, Boolean value) {
        if (value != null)
            builder.add(key, value);

        return builder;
    }
}
