package boosey;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;
import io.quarkus.jsonb.JsonbConfigCustomizer;

@Singleton
public class ReservationCustomAdapter implements JsonbConfigCustomizer {

    public void customize(JsonbConfig config) {
        config.withAdapters(new ReservationAdapter());
    }
}
