package ru.yandex.practicum.roles;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TeacherRoleTest {

    private final TeacherRole role = new TeacherRole();

    @Test
    void getName_returnsCorrectName() {
        assertEquals("Teacher", role.getName());
    }

    @Test
    void getPermissions_containsExpectedPermissions() {
        Set<Permission> permissions = role.getPermissions();
        assertEquals(9, permissions.size());

        boolean hasEnterSchool = false;
        boolean hasEnterTeachersRoom = false;
        int classEnterCount = 0;
        boolean hasLeaveAny = false;
        boolean hasWatch = false;
        boolean hasEdit = false;
        boolean hasEnterOwl = false;

        for (Permission p : permissions) {
            if (p instanceof SimplePermission) {
                SimplePermission sp = (SimplePermission) p;
                if (sp.allows(Action.ENTER, new Location(LocationType.SCHOOL), null, null)) {
                    hasEnterSchool = true;
                }
                if (sp.allows(Action.ENTER, new Location(LocationType.TEACHERS_ROOM), null, null)) {
                    hasEnterTeachersRoom = true;
                }
                if (sp.getLocationType() != null && sp.getLocationType().name().startsWith("CLASS_")) {
                    classEnterCount++;
                }
                if (sp.allows(Action.LEAVE, null, null, null)) {
                    hasLeaveAny = true;
                }
                if (sp.allows(Action.WATCH, null, null, null)) {
                    hasWatch = true;
                }
                if (sp.allows(Action.EDIT, null, null, null)) {
                    hasEdit = true;
                }
                if (sp.allows(Action.ENTER, new Location(LocationType.OWL_CABINET), null, null)) {
                    hasEnterOwl = true;
                }
            }
        }

        assertTrue(hasEnterSchool);
        assertTrue(hasEnterTeachersRoom);
        assertEquals(4, classEnterCount, "Разрешение на вход во все 4 класса");
        assertTrue(hasLeaveAny);
        assertTrue(hasWatch);
        assertTrue(hasEdit);
        assertFalse(hasEnterOwl, "Учитель НЕ должен иметь доступ в кабинет Совы");
    }
}