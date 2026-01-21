package ru.yandex.practicum.security;

import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.roles.TeacherRole;

public class TeacherPresentCondition implements Condition {
    @Override
    public boolean isSatisfied(User user, Location location, SecureContext ctx) {
        if (!(ctx instanceof SecureStateImpl state)) return false;

        if (state.getCurrentUser() != null &&
                state.getCurrentUser().getRole() instanceof TeacherRole &&
                state.occupied.contains(location)) {
            return true;
        }

        return false;
    }
}