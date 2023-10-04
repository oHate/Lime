package dev.ohate.lime.profile.friend;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.ohate.lime.Lime;
import dev.ohate.lime.profile.Profile;
import dev.ohate.lime.profile.cache.CachedProfile;
import dev.ohate.lime.profile.friend.models.Friendship;
import dev.ohate.lime.profile.friend.response.FriendRemoveResponse;
import dev.ohate.lime.profile.friend.response.FriendRequestCancelResponse;
import dev.ohate.lime.util.json.JsonUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Friend {

    public static final long REQUEST_EXPIRATION = TimeUnit.DAYS.toMillis(7L);
    public static final int MAX_OUTGOING_REQUESTS = 100;
    public static final int MAX_FRIENDS = 100;

    public static MongoCollection<Document> getCollection() {
        return Lime.getInstance().getDatabase().getCollection("friends");
    }

    public static List<Friendship> getFriends(UUID player) {
        Bson query = Filters.or(Filters.eq("sender", player.toString()), Filters.eq("receiver", player.toString()));
        List<Friendship> friendships = new ArrayList<>();

        for (Document document : getCollection().find(query)) {
            friendships.add(JsonUtil.readFromDocument(document, Friendship.class));
        }

        return friendships;
    }

    public static FriendRemoveResponse removeFriend(Profile player, Profile target) {
        Friendship friendship = player.getFriends().get(target.getUuid());

        if (friendship == null) {
            return FriendRemoveResponse.FAIL_NOT_ADDED;
        }

        friendship.delete();

        // TODO -> Make payload receive itself?
        player.unCacheFriendship(friendship);
        target.unCacheFriendship(friendship);

        CachedProfile.update(player, target);

        // TODO -> Broadcast Payload
        // TODO -> Save to cache

        return FriendRemoveResponse.SUCCESS;
    }

    public static FriendRequestCancelResponse removeRequest(Profile player, UUID targetId) {
        UUID playerId = player.getUuid();
        var cache = RequestCache.getFriendRequest();

        var outgoing = cache.getRawOutgoing(playerId);

        if (!outgoing.contains(targetUuid)) {
            return CancelResponse.NO_REQUEST;
        }

        cache.removeCache(playerId, targetUuid);

        return CancelResponse.SUCCESS;
    }

    public static AddResponse addRequest(Profile player, Profile target) {
        var playerId = player.getUuid();
        var targetUuid = target.getUuid();
        var cache = RequestCache.getFriendRequest();

        if (player.isFriends(target) || target.isFriends(player)) {
            return AddResponse.ALREADY_FRIENDS;
        }
        if (playerId.equals(targetUuid)) {
            return AddResponse.TARGET_IS_PLAYER;
        }
        if (!target.getOptionsProfile().isReceivingFriendRequests()) {
            return AddResponse.REQUESTS_TOGGLED;
        }
        if (player.isIgnoring(target)) {
            return AddResponse.TARGET_IGNORED;
        }
        if (target.isIgnoring(player)) {
            return AddResponse.PLAYER_IGNORED;
        }

        var outgoing = cache.getRawOutgoing(playerId);

        if (outgoing.size() >= MAX_OUTGOING_REQUESTS) {
            return AddResponse.MAX_OUTGOING;
        }
        if (outgoing.contains(targetUuid)) {
            return AddResponse.REQUEST_EXIST;
        }

        var incoming = cache.getRawIncoming(playerId);

        if (incoming.contains(targetUuid)) {
            return AddResponse.PENDING_REQUEST;
        }

        cache.pushCache(playerId, targetUuid);

        return AddResponse.SUCCESS;
    }

    public static AcceptResponse acceptRequest(Profile player, Profile target) {
        var playerId = player.getUuid();
        var targetUuid = target.getUuid();

        var cache = RequestCache.getFriendRequest();
        var incoming = cache.getRawIncoming(playerId);

        if (!incoming.contains(targetUuid)) {
            return AcceptResponse.NO_REQUEST;
        }

        cache.removeCache(playerId, targetUuid);
        cache.removeCache(targetUuid, playerId);

        if (player.getFriends().size() >= MAX_FRIENDS) {
            return AcceptResponse.PLAYER_MAX_FRIENDS;
        }
        if (target.getFriends().size() >= MAX_FRIENDS) {
            return AcceptResponse.TARGET_MAX_FRIENDS;
        }
        if (player.isIgnoring(target)) {
            return AcceptResponse.TARGET_IGNORED;
        }
        if (target.isIgnoring(player)) {
            return AcceptResponse.PLAYER_IGNORED;
        }

        player.addFriend(target);
        target.addFriend(player);

        player.save();
        target.save();

        ProfileCache.modifyProfile(player);
        ProfileCache.modifyProfile(target);

        return AcceptResponse.SUCCESS;
    }

}
