package dev.ohate.lime.profile.model;

public enum ProfileAction {
    ADD,
    REMOVE,
    EXPIRE;

    public ProfileAction getAction(ProfileObject object) {
        return object.isRemoved() ? object.isPermanent() ? ProfileAction.REMOVE : ProfileAction.EXPIRE : ProfileAction.ADD;
    }

}
