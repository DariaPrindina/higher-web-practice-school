package ru.yandex.practicum.security;

import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.*;

public class TeacherPresentCondition implements Condition {

    @Override
    public boolean isSatisfied(User user, Location location, SecureContext ctx) {
        if (!(ctx instanceof SecureStateImpl state)) return false;

        return state.isTeacherPresent(location);
    }
}