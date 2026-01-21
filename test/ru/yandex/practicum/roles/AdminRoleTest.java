package ru.yandex.practicum.roles;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdminRoleTest {

    private final AdminRole role = new AdminRole();

    @Test
    void getName_returnsCorrectName() {
        assertEquals("Admin", role.getName());
    }

    @Test
    void getPermissions_containsExpectedPermissions() {
        Set<Permission> permissions = role.getPermissions();
        assertEquals(10, permissions.size());

        boolean hasEnterSchool = false;
        boolean hasEnterOwl = false;
        boolean hasEnterTeachersRoom = false;
        int classEnterCount = 0;
        boolean hasLeaveAny = false;
        boolean hasWatch = false;
        boolean hasEdit = false;

        for (Permission p : permissions) {
            if (p instanceof SimplePermission sp) {
                if (sp.getAction() == Action.ENTER && sp.getLocationType() == LocationType.SCHOOL) {
                    hasEnterSchool = true;
                }
                if (sp.getAction() == Action.ENTER && sp.getLocationType() == LocationType.OWL_CABINET) {
                    hasEnterOwl = true;
                }
                if (sp.getAction() == Action.ENTER && sp.getLocationType() == LocationType.TEACHERS_ROOM) {
                    hasEnterTeachersRoom = true;
                }
                if (sp.getLocationType() != null && sp.getLocationType().name().startsWith("CLASS_")) {
                    classEnterCount++;
                }
                if (sp.getAction() == Action.LEAVE && sp.getLocationType() == null) {
                    hasLeaveAny = true;
                }
                if (sp.getAction() == Action.WATCH && sp.getLocationType() == null) {
                    hasWatch = true;
                }
                if (sp.getAction() == Action.EDIT && sp.getLocationType() == null) {
                    hasEdit = true;
                }
            }
        }

        assertTrue(hasEnterSchool);
        assertTrue(hasEnterOwl);
        assertTrue(hasEnterTeachersRoom);
        assertEquals(4, classEnterCount, "Админ должен иметь доступ во все классы");
        assertTrue(hasLeaveAny);
        assertTrue(hasWatch);
        assertTrue(hasEdit);
    }
}