package dev.ohate.lime.log;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;

@Getter
@Builder
public class Rollback {

    private Instant before;
    private Instant after;
    private String exclude;

}
