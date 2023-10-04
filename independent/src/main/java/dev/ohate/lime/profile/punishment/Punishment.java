package dev.ohate.lime.profile.punishment;

import dev.ohate.lime.profile.model.ProfileObject;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Punishment extends ProfileObject implements Comparable<Punishment> {

    private final PunishmentType type;

    public Punishment(
            UUID playerId,
            String origin,
            UUID addedById,
            String addedReason,
            int duration,
            PunishmentType type) {
        super(playerId, origin, addedById, addedReason, duration);

        this.type = type;
    }

    public String getContext() {
        if (isPermanent()) {
            return isRemoved() ? type.getUndoContext() : "permanently " + type.getContext();
        } else {
            return isRemoved() ? type.getUndoContext() : "temporarily " + type.getContext();
        }
    }

    @Override
    public int compareTo(Punishment punishment) {
        int isActiveComparison = Boolean.compare(punishment.isActive(), isActive());

        if (isActiveComparison != 0) {
            return isActiveComparison;
        }

        int typeComparison = getType().compareTo(punishment.getType());

        if (typeComparison != 0) {
            return typeComparison;
        }

        return punishment.getAddedAt().compareTo(getAddedAt());
    }

}
