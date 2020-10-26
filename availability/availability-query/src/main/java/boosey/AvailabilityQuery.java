package boosey;

import javax.inject.Singleton;
import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Multi;

@Singleton
public class AvailabilityQuery extends MutinyAvailabilityQueryGrpc.AvailabilityQueryImplBase {

    public Multi<AvailabilityGRPC> listAll() {
        return Multi.createFrom().emitter(emitter -> {
            Availability.streamAll()
                .forEach(x -> {
                    Availability a = (Availability)x;
                    emitter.emit(
                        AvailabilityGRPC.newBuilder()
                        .setId(a.getId())
                        .setResourceId(a.getResourceId())
                        .setResourceName(a.getResourceName())
                        .setOwnerName(a.getOwnerName())
                        .setStartTime(a.getStartTime().toString())
                        .setEndTime(a.getEndTime().toString())
                        .build()
                    );
                });
            emitter.complete(); 
        }); 
    }

    public Uni<CountReply> count(CountRequest r) {
        return Uni.createFrom().item(
                CountReply.newBuilder()
                .setCount(Availability.count())
                .build());
    }

    public Uni<ExistsReply> exists(ExistsRequest request) {
        return Uni.createFrom().item(
                ExistsReply.newBuilder()
                .setExists(Availability.count("availabilityId", request.getId()) > 0)
                .build()
            );
    }

    public Multi<AvailabilityGRPC> listAvailabilityForResource(
                ListAvailabilityForResourceRequest request) {

        return Multi.createFrom().emitter(emitter -> {
            Availability.<Availability>stream("resourceId = ?1", request.getId())
                .forEach(a -> {
                    emitter.emit(
                        AvailabilityGRPC.newBuilder()
                        .setId(a.getId())
                        .setResourceId(a.getResourceId())
                        .setResourceName(a.getResourceName())
                        .setOwnerName(a.getOwnerName())
                        .setStartTime(a.getStartTime().toString())
                        .setEndTime(a.getEndTime().toString())
                        .build()
                    );
                });
            emitter.complete(); 
        }); 
    }
}