package boosey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import boosey.resource.Availability;
import boosey.resource.Resource;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/resource-query")
@ApplicationScoped
public class ResourceQueryRest {

    @GET
    @Path("/")    
    @Produces(MediaType.APPLICATION_JSON)      
    public Uni<Response> listAll() {
        List<Resource> l = Optional
                            .<List<Resource>>ofNullable(Resource.listAll())
                            .orElse(new ArrayList<Resource>());                         
        if (l != null)
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.jsonbResourceIdOnly().toJson(l))
                    .build()); 
        else
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.jsonbResourceIdOnly().toJson(new ArrayList<>()))
                    .build());                       
    }

    @GET
    @Path("/existsByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Response> existsByName(@PathParam("resourceName") String name) {
        log.info("query existsByName: " + name);
        if (Resource.existsByName(name)) {
            log.info("Resource exists");
            return Uni.createFrom().item(Response.ok().build());
        } else {
            log.info("Resource does not exist");
            return Uni.createFrom().item(Response.status(Status.NOT_FOUND).build());
            // throw new NotFoundException();  
        }                 
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Uni<Response> getResource(@PathParam("id") String id) {
        Resource r = Resource.findById(id);
        if (r != null)
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.jsonbResourceIdOnly().toJson(r))
                    .build());
        else
            throw new NotFoundException();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)    
    public Uni<Response> count() {

        try {        
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Long.valueOf(Resource.count()))
                    .build());
        } catch (NotFoundException e) {
            throw e;
        }        
    }

    @GET
    @Path("/{id}/exists")
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Response> exists(@PathParam("id") String id) {
        
        if (Resource.count("id", id) > 0)
            return Uni.createFrom().item(Response.ok().build());
        else
            throw new NotFoundException();
    }

    @GET
    @Path("/findByName/{resourceName}")
    @Produces(MediaType.APPLICATION_JSON)     
    public Uni<Response> findByName(@PathParam("resourceName") String name) {
        try { 
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.jsonbResourceIdOnly().toJson(
                            Resource.findByName(name)))
                    .build()); 

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }            
    }

    // Availability API

    @GET
    @Path("/availability")    
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> listAllAvailability() {

        try { 
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.jsonbResourceIdOnly().toJson(Availability.listAll()))
                    .build());

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }          

    }

    @GET
    @Path("/{resourceId}/availability")    
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> listResourceAvailability(@PathParam("resourceId") String resourceId) {

        try { 

        return Uni.createFrom().item(
            Response
                .ok()
                .entity(Availability.jsonbResourceIdOnly().toJson(
                        Resource.<Resource>findById(resourceId).getAvailabilities()))
                .build()); 

        } catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }

    @GET
    @Path("/availability/{id}")
    @Produces(MediaType.APPLICATION_JSON)       
    public Uni<Response> getAvailability(@PathParam("id") String id) {

        Availability a = Availability.<Availability>findById(id);

        if (a != null) {
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.jsonbResourceIdOnly().toJson(a))
                    .build());            
        } else {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("/availability/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> countAllAvailability() {

        try {        
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Availability.count())
                    .build());
        } catch (NotFoundException e) {
            throw e;
        }
    }

    @GET
    @Path("/{resourceId}/availability/count")    
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> countResourceAvailability(@PathParam("resourceId") String resourceId) {
        try {
            return Uni.createFrom().item(
                Response
                    .ok()
                    .entity(Resource.<Resource>findById(resourceId).getAvailabilities().size())
                    .build());
        } catch (NotFoundException e) {
            throw e;
        }

    }

    @GET
    @Path("/availability/exists/{id}")
    @Produces(MediaType.TEXT_PLAIN)       
    public Uni<Response> existsAvailability(@PathParam("id") String id) {   
        if (Availability.count("id", id) > 0)
            return Uni.createFrom().item(Response.ok().build());
        else
            throw new NotFoundException();
    }  
}