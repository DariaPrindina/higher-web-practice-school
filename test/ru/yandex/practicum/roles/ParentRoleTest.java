package ru.yandex.practicum.roles;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.security.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParentRoleTest {

    private final ParentRole role = new ParentRole();

    @Test
    void getName_returnsCorrectName() {
        assertEquals("Parent", role.getName());
    }

    @Test
    void getPermissions_containsExpectedPermissions() {
        Set<Permission> permissions = role.getPermissions();
        assertEquals(3, permissions.size());

        boolean hasEnterSchool = false;
        boolean hasLeaveAny = false;
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
            }
        }

        assertTrue(hasEnterSchool, "Должно быть разрешение ENTER в SCHOOL");
        assertTrue(hasLeaveAny, "Должно быть разрешение LEAVE из любой локации");
        assertTrue(hasSimpleWatch, "Должно быть простое разрешение WATCH");
    }

    @Test
    void permissionsAreUnmodifiable() {
        Set<Permission> permissions = role.getPermissions();
        assertThrows(UnsupportedOperationException.class,
                () -> permissions.add(new SimplePermission(Action.ENTER, LocationType.SCHOOL)));
    }
}