package ru.yandex.practicum.security;

import ru.yandex.practicum.model.*;

public class OwnChildJournalCondition implements Condition {
    @Override
    public boolean isSatisfied(User user, Location location, SecureContext context) {
        String child = user.getChildLogin();
        if (child == null) return false;

        return true;
    }
}