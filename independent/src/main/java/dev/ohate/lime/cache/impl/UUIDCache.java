package dev.ohate.lime.cache.impl;

import com.mongodb.client.model.Projections;
import dev.ohate.lime.Lime;
import dev.ohate.lime.cache.Cache;
import dev.ohate.lime.profile.Profile;
import dev.ohate.swift.util.Redis;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class UUIDCache implements Cache<UUID> {

    private static final Bson PROJECTIONS = Projections.fields(Projections.include("_id", "username"));
    private static final String KEY = "lime:uuid-cache";

    private final Redis redis;

    public UUIDCache(Redis redis) {
        this.redis = redis;
    }

    @Override
    public UUID get(Object username) {
        return UUID.fromString(redis.runRedisCommand(jedis -> jedis.hget(
                KEY,
                username.toString().toLowerCase(Locale.ENGLISH)
        )));
    }

    @Override
    public void update(String username, UUID uuid) {
        redis.runRedisCommand(jedis -> jedis.hset(
                KEY,
                username.toLowerCase(Locale.ENGLISH),
                uuid.toString()
        ));
    }

    @Override
    public Map<String, UUID> bulkLoad(List<Object> keys) {
        String[] keyArray = keys.stream()
                .map(obj -> obj.toString().toLowerCase(Locale.ENGLISH))
                .toArray(String[]::new);

        List<String> uuids = Lime.getInstance()
                .getRedis()
                .runRedisCommand(jedis -> jedis.hmget(KEY, keyArray));

        Map<UUID, String> nameToUuid = new HashMap<>();

        for (int i = 0; i < uuids.size(); i++) {
            String name = keys.get(i).toString().toLowerCase(Locale.ENGLISH);


            nameToUuid.put(uuids.get(i), name == null ? "Unknown" : name);
        }

        return nameToUuid;
    }

    @Override
    public void purge() {
        redis.runRedisCommand(jedis -> jedis.del(KEY));
    }

    @Override
    public void rebuild() {
        Map<String, String> nameToUuid = new HashMap<>();

        for (Document document : Profile.getCollection().find().projection(PROJECTIONS)) {
            String uuid = document.get("_id").toString();
            String name = document.get("username").toString();

            nameToUuid.put(name.toLowerCase(Locale.ENGLISH), uuid);
        }

        redis.runRedisCommand(jedis -> jedis.hset(KEY, nameToUuid));
    }

}
