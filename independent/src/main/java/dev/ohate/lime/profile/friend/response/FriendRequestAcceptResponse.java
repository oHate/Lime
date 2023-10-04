package dev.ohate.lime.profile.friend.response;

public enum FriendRequestAcceptResponse {
    SUCCESS,
    FAIL_NO_REQUEST,
    FAIL_PLAYER_MAX_FRIENDS,
    FAIL_TARGET_MAX_FRIENDS,
    FAIL_TARGET_IGNORED,
    FAIL_PLAYER_IGNORED
}
