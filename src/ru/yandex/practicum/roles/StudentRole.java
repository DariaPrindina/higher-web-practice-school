package ru.yandex.practicum.roles;

import ru.yandex.practicum.model.*;
import ru.yandex.practicum.security.ConditionalPermission;
import ru.yandex.practicum.security.TeacherPresentCondition;

import java.util.Set;

public class StudentRole extends AbstractRole {
    public StudentRole() {
        super("Student", Set.of(
                new SimplePermission(Action.ENTER, LocationType.SCHOOL),
                new SimplePermission(Action.LEAVE, null),
                new ConditionalPermission(Action.ENTER, LocationType.CLASS_A, new TeacherPresentCondition()),
                new ConditionalPermission(Action.ENTER, LocationType.CLASS_B, new TeacherPresentCondition()),
                new ConditionalPermission(Action.ENTER, LocationType.CLASS_C, new TeacherPresentCondition()),
                new ConditionalPermission(Action.ENTER, LocationType.CLASS_D, new TeacherPresentCondition()),
                new SimplePermission(Action.WATCH, null)        ));
    }
}