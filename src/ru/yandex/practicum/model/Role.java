package ru.yandex.practicum.model;

import java.util.Set;

public interface Role {
    String getName();
    Set<Permission> getPermissions();
}
