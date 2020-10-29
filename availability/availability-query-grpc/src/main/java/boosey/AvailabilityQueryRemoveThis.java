package boosey;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import boosey.availability.Availability;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import io.smallrye.mutiny.Multi;

@Singleton
@Slf4j
public class AvailabilityQueryRemoveThis extends MutinyAvailabilityQueryGrpc.AvailabilityQueryImplBase {

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

    @Transactional
    public Uni<CountReply> count(CountRequest r) {
        log.info("In Count method");
        log.info("Availability count: " + Availability.count());

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