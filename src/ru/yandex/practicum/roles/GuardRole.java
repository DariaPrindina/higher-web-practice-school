package ru.yandex.practicum.roles;

import ru.yandex.practicum.model.*;

import java.util.Set;

public class GuardRole extends AbstractRole {
    public GuardRole() {
        super("Guard", Set.of(
                new SimplePermission(Action.ENTER, LocationType.SCHOOL),
                new SimplePermission(Action.LEAVE, LocationType.SCHOOL)
        ));
    }
}