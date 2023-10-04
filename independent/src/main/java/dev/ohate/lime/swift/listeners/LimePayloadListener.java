package dev.ohate.lime.swift.listeners;

import dev.ohate.lime.profile.Profile;
import dev.ohate.lime.profile.ProfileUtil;
import dev.ohate.lime.swift.payloads.bulk.WipePunishmentsPayload;
import dev.ohate.swift.handler.PayloadHandler;
import dev.ohate.swift.payload.PayloadListener;

import java.time.Instant;

public class LimePayloadListener implements PayloadListener {

    @PayloadHandler
    public void onWipePunishments(WipePunishmentsPayload payload) {
        for (Profile profile : ProfileUtil.getLocalProfileCache()) {
            profile.getPunishments().removeIf(punishment -> {
                if (!payload.getTypes().contains(punishment.getType())) {
                    return false;
                }

                punishment.remove(payload.getRemover(), Instant.now(), payload.getReason());

                return payload.isDeletePunishments();
            });
        }
    }

}
