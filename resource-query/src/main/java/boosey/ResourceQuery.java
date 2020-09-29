package boosey;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
// import org.jboss.logging.Logger;

@Path("/resources")
public class ResourceQuery {
    // private static final Logger log = Logger.getLogger(ResourceQuery.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Resource> list() {

        return Resource.listAll();
    }
}