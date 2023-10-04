package dev.ohate.lime.util;

import lombok.Data;

@Data
public class LimeConfig {

    private final String mongoUri = "mongodb://127.0.0.1:27017/";
    private final String redisUri = "redis://127.0.0.1:6379/0";
    private final String networkName = "development";
    private final String unitName = "Staging-1";

}
