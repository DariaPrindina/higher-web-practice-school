package ru.yandex.practicum.security;

import ru.yandex.practicum.model.Location;
import ru.yandex.practicum.model.User;

public interface Condition {
    boolean isSatisfied(User user, Location location, SecureContext context);
}