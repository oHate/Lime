package dev.ohate.lime.profile.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishmentType {
    BLACKLIST(
            "blacklist",
            "blacklisted",
            "unblacklisted"
    ),

    BAN(
            "ban",
            "banned",
            "unbanned"
    ),

    MUTE(
            "mute",
            "muted",
            "unmuted"
    ),

    WARN(
            "warn",
            "warned",
            ""
    );

    private final String readable;
    private final String context;
    private final String undoContext;

    public static PunishmentType fromString(String punishmentType) {
        for (PunishmentType type : values()) {
            if (type.name().equalsIgnoreCase(punishmentType)) {
                return type;
            }
        }

        return null;
    }

}
