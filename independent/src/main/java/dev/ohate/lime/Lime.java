package dev.ohate.lime;

import com.google.gson.JsonSyntaxException;
import com.mongodb.client.MongoDatabase;
import dev.ohate.lime.mongo.Mongo;
import dev.ohate.lime.util.LimeConfig;
import dev.ohate.lime.util.json.JsonUtil;
import dev.ohate.swift.Swift;
import dev.ohate.swift.payload.PayloadRegistry;
import dev.ohate.swift.util.Redis;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
public class Lime {

    @Getter
    private static Lime instance;

    private LimeConfig config;
    private final Swift swift;
    private final Redis redis;
    private final Mongo mongo;
    private final MongoDatabase database;

    private Lime(File configFile) {
        loadConfig(configFile);

        swift = new Swift(config.getNetworkName(), config.getUnitName(), config.getRedisUri());

        redis = swift.getRedis();

        mongo = new Mongo();
        mongo.connect(config.getMongoUri());

        database = mongo.getClient().getDatabase(config.getNetworkName());
    }

    private void loadConfig(File configFile) {
        if (configFile == null) {
            configFile = new File("config", "core.json");
        }

        if (!configFile.exists()) {
            config = new LimeConfig();

            try {
                Files.writeString(configFile.toPath(), JsonUtil.writeToPrettyJson(config));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                config = JsonUtil.readFromJson(new String(Files.readAllBytes(configFile.toPath())), LimeConfig.class);
            } catch (IOException | JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void initializeLime() {
        initializeLime(null);
    }

    public static void initializeLime(File configFile) {
        if (instance != null) {
            throw new RuntimeException("Lime has already been initialized.");
        }

        instance = new Lime(configFile);
    }

}
