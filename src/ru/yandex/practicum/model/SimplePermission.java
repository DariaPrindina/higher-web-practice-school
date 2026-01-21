package ru.yandex.practicum.model;

import ru.yandex.practicum.security.SecureContext;

public class SimplePermission implements Permission {

    private final Action action;
    private final LocationType locationType;

    public SimplePermission(Action action, LocationType locationType) {
        this.action = action;
        this.locationType = locationType;
    }

    @Override
    public boolean allows(Action action, Location location, User user, SecureContext context) {
        if (this.action != action) return false;
        if (locationType == null) return true;
        return location != null && location.getType() == locationType;
    }

    public Action getAction() {
        return action;
    }

    public LocationType getLocationType() {
        return locationType;
    }

}