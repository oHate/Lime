package dev.ohate.lime.redis;

import dev.ohate.lime.Lime;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UUIDCache {

//    public static final String NAME_TO_UUID = "flare:name-uuid";
//    public static final String UUID_TO_NAME = "flare:uuid-name";
//
//    public static String getName(UUID uuid) {
////        return Lime.getInstance().runRedisCommand(jedis -> jedis.hget(UUID_TO_NAME, uuid.toString()));
//    }
//
//    public static UUID getUuid(String name) {
//        String uuid = Core.instance().runRedisCommand(jedis -> jedis.hget(NAME_TO_UUID, name.toLowerCase()));
//
//        if (uuid != null) {
//            return UUID.fromString(uuid);
//        }
//
//        return null;
//    }
//
//    public static Map<UUID, String> bulkRetrieveUuidToName(List<UUID> uuids) {
//        String[] keys = uuids.stream().map(UUID::toString).toArray(String[]::new);
//        List<String> names = Core.instance().runRedisCommand(jedis -> jedis.hmget(UUID_TO_NAME, keys));
//        Map<UUID, String> uuidToName = new HashMap<>();
//
//        for (int i = 0; i < names.size(); i++) {
//            String name = names.get(i);
//            uuidToName.put(uuids.get(i), name == null ? "Unknown" : name);
//        }
//
//        return uuidToName;
//    }
//
//    public static void updateCache(UUID uuid, String name) {
//        Core.instance().runRedisCommand(jedis -> {
//            String oldName = jedis.hget(UUID_TO_NAME, uuid.toString());
//            Pipeline pipeline = jedis.pipelined();
//
//            if (oldName != null && !oldName.equalsIgnoreCase(name)) {
//                pipeline.hdel(NAME_TO_UUID, oldName);
//            }
//
//            pipeline.hset(NAME_TO_UUID, name.toLowerCase(), uuid.toString());
//            pipeline.hset(UUID_TO_NAME, uuid.toString(), name);
//            pipeline.sync();
//
//            return null;
//        });
//    }
//
//    public static void purgeCache() {
//        Core.instance().runRedisCommand(jedis -> jedis.del(NAME_TO_UUID, UUID_TO_NAME));
//    }
//
//    public static CompletableFuture<Long> rebuildCache() {
//        return CompletableFuture.supplyAsync(() -> {
//            purgeCache();
//
//            Map<String, String> nameToUuid = new HashMap<>();
//            Map<String, String> uuidToName = new HashMap<>();
//
//            for (var doc : Profile.collection().find()) {
//                String uuid = doc.get("_id").toString();
//                String name = doc.get("username").toString();
//
//                nameToUuid.put(name.toLowerCase(), uuid);
//                uuidToName.put(uuid, name);
//            }
//
//            Core.instance().runRedisCommand(jedis -> {
//                Pipeline pipeline = jedis.pipelined();
//
//                pipeline.hset(NAME_TO_UUID, nameToUuid);
//                pipeline.hset(UUID_TO_NAME, uuidToName);
//
//                pipeline.sync();
//                return null;
//            });
//
//            return System.currentTimeMillis();
//        });
//    }

}
