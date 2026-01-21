package ru.yandex.practicum.security;

import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.Location;
import ru.yandex.practicum.model.User;

public class OwnChildAndTeacherPresentCondition implements Condition {
    @Override
    public boolean isSatisfied(User user, Location loc, SecureContext ctx) {
        if (!(ctx instanceof SecureStateImpl state)) return false;

        String child = user.getChildLogin();
        if (child == null) return false;

        return new TeacherPresentCondition().isSatisfied(user, loc, ctx);
    }
}
