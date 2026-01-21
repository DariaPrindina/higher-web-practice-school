package ru.yandex.practicum.roles;

import ru.yandex.practicum.model.*;

import java.util.Set;

public class ChiefRole extends AbstractRole {
    public ChiefRole() {
        super("Chief", Set.of(
                new SimplePermission(Action.ENTER, LocationType.SCHOOL),
                new SimplePermission(Action.ENTER, LocationType.OWL_CABINET),
                new SimplePermission(Action.ENTER, LocationType.TEACHERS_ROOM),
                new SimplePermission(Action.ENTER, LocationType.CLASS_A),
                new SimplePermission(Action.ENTER, LocationType.CLASS_B),
                new SimplePermission(Action.ENTER, LocationType.CLASS_C),
                new SimplePermission(Action.ENTER, LocationType.CLASS_D),
                new SimplePermission(Action.LEAVE, null),
                new SimplePermission(Action.WATCH, null),
                new SimplePermission(Action.EDIT, null)
        ));
    }
}