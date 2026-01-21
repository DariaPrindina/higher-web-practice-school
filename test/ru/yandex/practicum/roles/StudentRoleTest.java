package ru.yandex.practicum.roles;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.security.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentRoleTest {

    private final StudentRole role = new StudentRole();

    @Test
    void getName_returnsCorrectName() {
        assertEquals("Student", role.getName());
    }

    @Test
    void getPermissions_containsExpectedPermissions() {
        Set<Permission> permissions = role.getPermissions();
        assertEquals(7, permissions.size());

        boolean hasEnterSchool = false;
        boolean hasLeaveAny = false;
        int classEnterCount = 0;
        boolean hasSimpleWatch = false;

        for (Permission p : permissions) {
            if (p instanceof SimplePermission sp) {
                if (sp.getAction() == Action.ENTER && sp.getLocationType() == LocationType.SCHOOL) {
                    hasEnterSchool = true;
                }
                if (sp.getAction() == Action.LEAVE && sp.getLocationType() == null) {
                    hasLeaveAny = true;
                }
                if (sp.getAction() == Action.WATCH && sp.getLocationType() == null) {
                    hasSimpleWatch = true;
                }
            } else if (p instanceof ConditionalPermission cp) {
                if (cp.getLocationType() != null && cp.getLocationType().name().startsWith("CLASS_")) {
                    classEnterCount++;
                    assertInstanceOf(TeacherPresentCondition.class, cp.getCondition());
                }
            }
        }

        assertTrue(hasEnterSchool);
        assertTrue(hasLeaveAny);
        assertEquals(4, classEnterCount, "Должно быть 4 условных разрешения на вход в классы A,B,C,D");
        assertTrue(hasSimpleWatch, "Должно быть простое разрешение WATCH");
    }
}