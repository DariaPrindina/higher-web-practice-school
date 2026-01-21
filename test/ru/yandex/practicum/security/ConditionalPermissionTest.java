package ru.yandex.practicum.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.roles.StudentRole;
import ru.yandex.practicum.roles.TeacherRole;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalPermissionTest {

    private SecureStateImpl context;
    private User student;
    private ConditionalPermission permission;

    @BeforeEach
    void setUp() {
        context = new SecureStateImpl();
        student = new User("roo", "5%", new StudentRole());

        permission = new ConditionalPermission(
                Action.ENTER,
                LocationType.CLASS_A,
                new TeacherPresentCondition()
        );
    }

    @Test
    void allows_returnsFalse_whenActionDoesNotMatch() {
        boolean allowed = permission.allows(Action.WATCH, null, student, context);
        assertFalse(allowed, "Действие не совпадает → false");
    }

    @Test
    void allows_returnsFalse_whenLocationTypeDoesNotMatch() {
        Location classB = new Location(LocationType.CLASS_B, "B");
        boolean allowed = permission.allows(Action.ENTER, classB, student, context);
        assertFalse(allowed, "Тип локации не CLASS_A → false");
    }

    @Test
    void allows_returnsFalse_whenConditionNotSatisfied() {
        Location classA = new Location(LocationType.CLASS_A, "A");
        boolean allowed = permission.allows(Action.ENTER, classA, student, context);
        assertFalse(allowed, "Учитель не присутствует → false");
    }

    @Test
    void allows_returnsTrue_whenConditionSatisfied() {
        User teacher = new User("piglet", "2@", new TeacherRole());
        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "A");

        Location classA = new Location(LocationType.CLASS_A, "A");
        boolean allowed = permission.allows(Action.ENTER, classA, student, context);
        assertTrue(allowed, "Учитель присутствует в классе → true");
    }

    @Test
    void allows_returnsTrue_whenLocationIsNullAndConditionSatisfied() {
        ConditionalPermission watchPermission = new ConditionalPermission(
                Action.WATCH,
                null,
                new TeacherPresentCondition()
        );

        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "school");

        boolean allowed = watchPermission.allows(Action.WATCH, null, student, context);
        assertTrue(allowed);
    }

    @Test
    void allows_returnsFalse_whenContextIsWrongType() {
        boolean allowed = permission.allows(Action.ENTER, null, student, null);
        assertFalse(allowed);
    }
}