package ru.yandex.practicum.security;

import ru.yandex.practicum.model.*;

public class ConditionalPermission implements Permission {
    private final Action action;
    private final LocationType locationType;
    private final Condition condition;

    public ConditionalPermission(Action action, LocationType locationType, Condition condition) {
        this.action = action;
        this.locationType = locationType;
        this.condition = condition;
    }

    @Override
    public boolean allows(Action action, Location location, User user, SecureContext context) {
        if (this.action != action) return false;
        if (locationType != null && (location == null || location.getType() != locationType)) {
            return false;
        }
        return condition.isSatisfied(user, location, context);
    }
}