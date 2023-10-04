package dev.ohate.lime.profile.cache;

import dev.ohate.lime.Lime;
import dev.ohate.lime.profile.Profile;
import dev.ohate.lime.util.json.JsonUtil;
import dev.ohate.swift.util.Redis;
import lombok.Data;
import redis.clients.jedis.Pipeline;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
public class CachedProfile {

    private static final String KEY = "lime:profile-cache";

    private Profile profile;
    private String serverId;
    private UUID lastMessaged;

    public CachedProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfile(Profile newProfile) {
        if (newProfile == null)
            throw new IllegalArgumentException("A CachedProfile's profile cannot be null.");

        this.profile = newProfile;
    }

    public static CachedProfile getCachedProfile(UUID source) {
        String json = Lime.getInstance()
                .getRedis()
                .runRedisCommand(redis -> redis.hget(KEY, source.toString()));

        return json == null ? null : JsonUtil.readFromJson(json, CachedProfile.class);
    }

    public static CompletableFuture<Void> resetCache() {
        return CompletableFuture.runAsync(() -> Lime.getInstance()
                .getRedis()
                .runRedisCommand(redis -> redis.del(KEY)));
    }

    public static CompletableFuture<Void> update(Profile... profiles) {
        if (profiles.length == 0) {
            return CompletableFuture.completedFuture(null);
        }

        final Redis redis = Lime.getInstance().getRedis();

        return CompletableFuture.supplyAsync(() -> redis.runRedisCommand(jedis -> {
            Pipeline pipeline = jedis.pipelined();

            for (Profile profile : profiles) {
                pipeline.hget(KEY, profile.getUuid().toString());
            }

            return pipeline.syncAndReturnAll();
        })).thenAccept(results -> redis.runRedisCommand(jedis -> {
            Pipeline pipeline = jedis.pipelined();

            for (int index = 0; index < results.size(); index++) {
                Object result = results.get(index);
                Profile profile = profiles[index];

                CachedProfile cachedProfile = new CachedProfile(profile);

                if (result != null) {
                    cachedProfile = JsonUtil.readFromJson((String) result, CachedProfile.class);
                    cachedProfile.setProfile(profile);
                }

                pipeline.hset(KEY, profile.getUuid().toString(), JsonUtil.writeToJson(cachedProfile));
            }

            pipeline.sync();
            return null;
        }));
    }

    public void upsert(Pipeline pipeline) {
        pipeline.hset(KEY, profile.getUuid().toString(), JsonUtil.writeToJson(this));
    }

    public CompletableFuture<Void> upsert() {
        return CompletableFuture.runAsync(() -> Lime.getInstance()
                .getRedis()
                .runRedisCommand(jedis -> jedis.hset(KEY, profile.getUuid().toString(), JsonUtil.writeToJson(this))));
    }

}