package dev.ohate.lime.profile;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import dev.ohate.lime.Lime;
import dev.ohate.lime.profile.friend.models.Friendship;
import dev.ohate.lime.profile.model.Permission;
import dev.ohate.lime.profile.model.Skin;
import dev.ohate.lime.profile.punishment.Punishment;
import dev.ohate.lime.profile.rank.Grant;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.*;

@Data
public class Profile {

    @SerializedName("_id")
    private final ObjectId id;
    private final UUID uuid;
    private String username;
    private String address;
    private Instant lastOnline;
    private Skin skin;

    private final Set<Permission> permissions;
    private final Set<UUID> siblings;
    private final Set<UUID> ignored;

    private final List<Punishment> punishments;
    private final List<Grant> grants;
    private final Map<UUID, Friendship> friends;

    public Profile(UUID uuid, String username) {
        this.id = new ObjectId();
        this.uuid = uuid;
        this.username = username;

        address = "";
        lastOnline = Instant.now();
        skin = Skin.UNKNOWN;

        punishments = new ArrayList<>();
        grants = new ArrayList<>();
        permissions = new HashSet<>();
        friends = new HashMap<>();
        siblings = new HashSet<>();
        ignored = new HashSet<>();
    }

    public static MongoCollection<Document> getCollection() {
        return Lime.getInstance().getDatabase().getCollection("profiles");
    }

    public Instant getCreationDate() {
        return id.getDate().toInstant();
    }

    public boolean isFriends(Profile profile) {
        return isFriends(profile.getUuid());
    }

    public boolean isFriends(UUID source) {
        return friends.containsKey(source);
    }

    public void cacheFriendship(Friendship friendship) {
        friends.put(friendship.getOther(uuid), friendship);
    }

    public void unCacheFriendship(Friendship friendship) {
        friends.remove(friendship.getOther(uuid));
    }

    public void addPermission(String permission, String scope) {
        permissions.add(new Permission(
                permission.toLowerCase(Locale.ENGLISH),
                scope.toLowerCase(Locale.ENGLISH)
        ));

        // TODO -> sync
    }

    public void removePermission(String permission, String scope) {
        permissions.removeIf(perm -> perm.getPermission().equalsIgnoreCase(permission) && perm.getScope().equalsIgnoreCase(scope));

        // TODO -> sync
    }

}
