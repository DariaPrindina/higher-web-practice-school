package ru.yandex.practicum.roles;

import ru.yandex.practicum.model.*;
import ru.yandex.practicum.security.ConditionalPermission;
import ru.yandex.practicum.security.OwnChildAndTeacherPresentCondition;

import java.util.Set;

public class ParentRole extends AbstractRole {
    public ParentRole() {
        super("Parent", Set.of(
                new SimplePermission(Action.ENTER, LocationType.SCHOOL),
                new SimplePermission(Action.LEAVE, null),
                new SimplePermission(Action.WATCH, null)        ));
    }
}