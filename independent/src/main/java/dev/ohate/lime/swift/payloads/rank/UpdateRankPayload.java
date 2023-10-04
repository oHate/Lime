package dev.ohate.lime.swift.payloads.rank;

import dev.ohate.lime.profile.rank.Rank;
import dev.ohate.swift.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRankPayload extends Payload {

    private final Rank newRank;

}
