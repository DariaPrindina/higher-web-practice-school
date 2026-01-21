package ru.yandex.practicum.model;

import ru.yandex.practicum.security.SecureContext;

public interface Permission {

    boolean allows(Action action, Location location, User user, SecureContext context);

}
