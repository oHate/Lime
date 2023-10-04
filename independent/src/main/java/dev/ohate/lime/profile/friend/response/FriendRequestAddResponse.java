package dev.ohate.lime.profile.friend.response;

public enum FriendRequestAddResponse {
    SUCCESS,
    FAIL_ALREADY_FRIENDS,
    FAIL_TARGET_IS_PLAYER,
    FAIL_MAX_OUTGOING,
    FAIL_REQUEST_EXIST,
    FAIL_PENDING_REQUEST,
    FAIL_REQUESTS_TOGGLED,
    FAIL_TARGET_IGNORED,
    FAIL_PLAYER_IGNORED
}
