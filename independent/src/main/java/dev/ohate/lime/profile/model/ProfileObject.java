package dev.ohate.lime.profile.model;

import com.google.gson.annotations.SerializedName;
import dev.ohate.lime.util.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
public class ProfileObject {

    @SerializedName("_id")
    private final ObjectId id;
    private final UUID playerId;
    private final String origin;

    private final UUID addedById;
    private final String addedReason;
    private final int duration;

    private UUID removedById;
    private Instant removedAt;
    private String removedReason;

    public ProfileObject(UUID playerId, String origin, UUID addedById, String addedReason, int duration) {
        this.id = new ObjectId();
        this.playerId = playerId;
        this.origin = origin;
        this.addedById = addedById;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public void remove(UUID removedById, Instant removedAt, String removedReason) {
        this.removedById = removedById;
        this.removedAt = removedAt;
        this.removedReason = removedReason;
    }

    public Instant getAddedAt() {
        return id.getDate().toInstant();
    }

    public void expire(String reason) {
        remove(null, getAddedAt().plusSeconds(duration), reason);
    }

    public boolean isRemoved() {
        return removedAt != null;
    }

    public boolean isPermanent() {
        return duration == -1;
    }

    public boolean isActive() {
        return !isRemoved() && (isPermanent() || !hasExpired());
    }

    public long getMillisRemaining() {
        return getAddedAt().plusSeconds(duration).minusMillis(System.currentTimeMillis()).toEpochMilli();
    }

    public boolean hasExpired() {
        return !isPermanent() && getAddedAt().plusSeconds(duration).isAfter(Instant.now());
    }

    public String getTimeRemaining() {
        if (isRemoved()) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.millisToTime(getMillisRemaining());
    }

}
