package boosey;

import javax.ejb.DuplicateKeyException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import boosey.ResourceSchedulerEvent.Source;
import boosey.ResourceSchedulerEvent.Type;
import boosey.owner.Owner;
import lombok.val;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.ws.rs.Consumes;
// import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

// @Slf4j
@Path("/owner-command")
@ApplicationScoped
public class OwnerCommand {
    @Inject @RestClient OwnerQueryClient query;
    @Inject @RestClient EventServiceClient events;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOwner(Owner owner) throws DuplicateKeyException {

        if (!existsByName(owner)) {
            val e = new ResourceSchedulerEvent<Owner>(
                        Type.ADD_OWNER,
                        Source.OWNER_API,
                        owner);    

            events.fire(e);  
            return Response.ok(owner.getId()).build();                        
        } else {
            return Response.status(Status.CONFLICT).build();            
        }
    }    

    private Boolean existsByName(Owner o) {
        return query.existsByName(o.getName()).readEntity(Boolean.class);
    }

    private Boolean exists(String ownerId) {
        return query.exists(ownerId).readEntity(Boolean.class);
    }

    public Boolean replaceOwner(String ownerId, Owner owner) {

        if (exists(ownerId)) {
            
            if (!owner.id.equalsIgnoreCase(ownerId))
                throw new RuntimeException("IDs aren't equal"); 

            val e = new ResourceSchedulerEvent<Owner>(
                        Type.REPLACE_OWNER,
                        Source.OWNER_API,
                        owner);  
                        
            events.fire(e);                        

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }   

    public Boolean deleteAllOwners() {

        val e = new ResourceSchedulerEvent<NoEventData>(
                    Type.DELETE_ALL_OWNERS,
                    Source.OWNER_API,
                    NoEventData.builder().build());  
                    
        events.fire(e);              

        return true;
    }    

    public Boolean deleteOwner(String ownerId) {

        if (exists(ownerId)) { 

        val e = new ResourceSchedulerEvent<ItemIdData>(
                    Type.DELETE_OWNER,
                    Source.OWNER_API,
                    ItemIdData.builder().id(ownerId).build());  
                    
        events.fire(e);                  

        } else {        
            throw new NotAcceptableException("Owner Does Not Exist");
        }

        return true;
    }    
}