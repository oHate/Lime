package dev.ohate.lime.swift.payloads.bulk;

import dev.ohate.lime.profile.punishment.PunishmentType;
import dev.ohate.swift.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class WipePunishmentsPayload extends Payload {

    public static final WipePunishmentsPayload DELETE_ALL_PUNISHMENTS = new WipePunishmentsPayload(
            Set.of(PunishmentType.BLACKLIST, PunishmentType.BAN, PunishmentType.MUTE, PunishmentType.WARN),
            null,
            null,
            null,
            "",
            true
    );

    private final Set<PunishmentType> types;
    private final Instant start;
    private final Instant end;
    private final UUID remover;
    private final String reason;
    private final boolean deletePunishments;

}
