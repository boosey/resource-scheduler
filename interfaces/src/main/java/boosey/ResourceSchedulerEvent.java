package boosey;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceSchedulerEvent<T> {
    
    public enum Type {
        ADD_RESOURCE,
        RESOURCE_ADDED,
        REPLACE_RESOURCE,
        RESOURCE_REPLACED,
        DELETE_ALL_RESOURCES,
        ALL_RESOURCES_DELETED,
        DELETE_RESOURCE,
        RESOURCE_DELETED,
        RESOURCE_COMMAND_COMPLETE,
        DEACTIVATE_RESOURCE,
        RESOURCE_DEACTIVATED,

        ADD_OWNER,
        OWNER_ADDED,
        REPLACE_OWNER,
        OWNER_REPLACED,
        DELETE_ALL_OWNERS,
        ALL_OWNERS_DELETED,
        DELETE_OWNER,
        OWNER_DELETED,
        OWNER_COMMAND_COMPLETE,

        ADD_AVAILABILITY,
        AVAILABILITY_ADDED,
        REPLACE_AVAILABILITY,
        AVAILABILITY_REPLACED,
        DELETE_ALL_AVAILABILITIES,
        ALL_AVAILABILITIES_DELETED,
        DELETE_AVAILABILITY,
        AVAILABILITY_DELETED,
        AVAILABILITY_COMMAND_COMPLETE,
        AVAILABILITY_ACTIVATED,
        AVAILABILITY_DEACTIVATED,

        ADD_REQUEST_USE,
        REQUEST_USE_ADDED,
        REPLACE_REQUEST_USE,
        REQUEST_USE_REPLACED,
        DELETE_ALL_REQUEST_USES,
        ALL_REQUEST_USES_DELETED,
        DELETE_REQUEST_USE,
        REQUEST_USE_DELETED,
        REQUEST_USE_COMMAND_COMPLETE,    

        ADD_ACCEPT_USE,
        ACCEPT_USE_ADDED,
        REPLACE_ACCEPT_USE,
        ACCEPT_USE_REPLACED,
        DELETE_ALL_ACCEPT_USES,
        ALL_ACCEPT_USES_DELETED,
        DELETE_ACCEPT_USE,
        ACCEPT_USE_DELETED,
        ACCEPT_USE_COMMAND_COMPLETE,                          

        ADD_RESERVATION,
        RESERVATION_ADDED,
        REPLACE_RESERVATION,
        RESERVATION_REPLACED,
        DELETE_ALL_RESERVATIONS,
        ALL_RESERVATIONS_DELETED,
        DELETE_RESERVATION,
        RESERVATION_DELETED,
        RESERVATION_COMMAND_COMPLETE,  
      
    }

    public enum Source {
        RESOURCE_API,
        HANDLE_ADD_RESOURCE,
        HANDLE_REPLACE_RESOURCE,
        HANDLE_DELETE_ALL_RESOURCES,
        HANDLE_DELETE_RESOURCE,
        HANDLE_RESOURCE_DEACTIVATED,

        OWNER_API,
        HANDLE_ADD_OWNER,
        HANDLE_REPLACE_OWNER,
        HANDLE_DELETE_ALL_OWNERS,
        HANDLE_DELETE_OWNER,

        AVAILABILITY_API,
        HANDLE_ADD_AVAILABILITY,
        HANDLE_REPLACE_AVAILABILITY,
        HANDLE_DELETE_ALL_AVAILABILITIES,
        HANDLE_DELETE_AVAILABILITY,
        HANDLE_AVAILABILITY_DEACTIVATED,
        HANDLE_AVAILABILITY_ACTIVATED,
        
        REQUEST_USE_API,
        HANDLE_ADD_REQUEST_USE,
        HANDLE_REPLACE_REQUEST_USE,
        HANDLE_DELETE_ALL_REQUEST_USES,
        HANDLE_DELETE_REQUEST_USE,
        
        ACCEPT_USE_API,
        HANDLE_ADD_ACCEPT_USE,
        HANDLE_REPLACE_ACCEPT_USE,
        HANDLE_DELETE_ALL_ACCEPT_USES,
        HANDLE_DELETE_ACCEPT_USE,
     
        RESERVATION_API,
        HANDLE_ADD_RESERVATION,
        HANDLE_REPLACE_RESERVATION,
        HANDLE_DELETE_ALL_RESERVATIONS,
        HANDLE_DELETE_RESERVATION,
        HANDLE_RESERVATION_DEACTIVATED,
        HANDLE_RESERVATION_ACTIVATED,
                                                
    }

    private  String eventId = generateUUID();
    private  Type eventType = null;    
    private  Source source = null;
    private  String specVersion = "1.0";
    private  String subject = null;
    private  T eventData = null;

    public ResourceSchedulerEvent(Type eventType, Source source, T eventData) {
        this.eventType = eventType;
        this.source = source;
        this.subject = "";
        this.eventData = eventData;
    }

    public ResourceSchedulerEvent(Type eventType, Source source, String subject, T eventData) {
        this.eventType = eventType;
        this.source = source;
        this.subject = subject;
        this.eventData = eventData;
    }

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    } 
}
