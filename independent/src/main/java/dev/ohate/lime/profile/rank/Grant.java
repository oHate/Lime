package dev.ohate.lime.profile.rank;

import dev.ohate.lime.profile.model.ProfileObject;
import dev.ohate.lime.util.Duration;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Grant extends ProfileObject implements Comparable<Grant> {

    private final String rank;

    public Grant(
            UUID playerId,
            String origin,
            UUID addedById,
            String addedReason,
            int duration,
            Rank rank
    ) {
        super(playerId, origin, addedById, addedReason, duration);

        this.rank = rank.getName();
    }

    public Rank getRank() {
        return Rank.getRank(rank);
    }

    public static Grant getDefaultGrant() {
        return new Grant(
                null,
                "",
                null,
                "Default Grant",
                Duration.PERMANENT,
                Rank.getDefaultRank()
        );
    }

    @Override
    public int compareTo(Grant grant) {
        int isActiveComparison = Boolean.compare(grant.isActive(), isActive());
        if (isActiveComparison != 0) {
            return isActiveComparison;
        }

        int typeComparison = getRank().compareTo(grant.getRank());
        if (typeComparison != 0) {
            return typeComparison;
        }

        return grant.getAddedAt().compareTo(getAddedAt());
    }

}