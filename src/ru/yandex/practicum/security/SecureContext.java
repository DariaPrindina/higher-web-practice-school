package ru.yandex.practicum.security;

import ru.yandex.practicum.model.*;

public interface SecureContext {
    User getCurrentUser();
    boolean isTeacherPresent(Location location);
    Journal getJournal();
}