package ru.yandex.practicum.security;

import ru.yandex.practicum.model.*;

public class AuthorizationService {

    public boolean isAllowed(User user, Action action, Location location, SecureContext context) {
        if (user == null) return false;

        for (Permission p : user.getRole().getPermissions()) {
            if (p.allows(action, location, user, context)) {
                return true;
            }
        }
        return false;
    }
}