package dev.ohate.lime.profile;

import com.mongodb.client.model.Filters;
import dev.ohate.lime.profile.cache.CachedProfile;
import dev.ohate.lime.util.json.JsonUtil;
import org.bson.Document;

import java.util.*;

public class ProfileUtil {

    private static final Map<UUID, Profile> PROFILE_CACHE = new HashMap<>();

    public static List<Profile> getLocalProfileCache() {
        return new ArrayList<>(PROFILE_CACHE.values());
    }

    public static void cacheProfileLocally(Profile profile) {
        PROFILE_CACHE.put(profile.getUuid(), profile);
    }

    public static Profile getOrCreateProfile(UUID source, String username) {
        Profile profile = getProfile(source);
        if (profile != null)
            return profile;

        return new Profile(source, username);
    }

    public static Profile getProfile(UUID source) {
        Profile locallyCachedProfile = getLocallyCachedProfile(source);
        if (locallyCachedProfile != null)
            return locallyCachedProfile;

        Profile redisProfile = getRedisCachedProfile(source);
        if (redisProfile != null)
            return redisProfile;

        Profile mongoProfile = getProfileFromMongo(source);
        if (mongoProfile != null)
            return mongoProfile;

        return null;
    }

    public static Profile getLocallyCachedProfile(UUID source) {
        return PROFILE_CACHE.get(source);
    }

    public static Profile getRedisCachedProfile(UUID source) {
        CachedProfile cachedProfile = CachedProfile.getCachedProfile(source);

        return cachedProfile == null ? null : cachedProfile.getProfile();
    }

    public static Profile getProfileFromMongo(UUID source) {
        Document document = Profile.getCollection().find(Filters.eq("uuid", source.toString())).first();

        return document == null ? null : JsonUtil.readFromDocument(document, Profile.class);
    }

}
