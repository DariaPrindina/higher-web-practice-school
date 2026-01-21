package ru.yandex.practicum.roles;

import ru.yandex.practicum.model.Permission;
import ru.yandex.practicum.model.Role;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractRole implements Role {
    protected final String name;
    protected final Set<Permission> permissions;

    public AbstractRole(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions != null
                ? Collections.unmodifiableSet(permissions)
                : Collections.emptySet();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return name;
    }
}