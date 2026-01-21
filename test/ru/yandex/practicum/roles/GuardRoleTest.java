package ru.yandex.practicum.roles;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.security.ConditionalPermission;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GuardRoleTest {

    private final GuardRole role = new GuardRole();

    @Test
    void getName_returnsCorrectName() {
        assertEquals("Guard", role.getName());
    }

    @Test
    void getPermissions_containsOnlyExpectedPermissions() {
        Set<Permission> permissions = role.getPermissions();
        assertEquals(2, permissions.size());

        boolean hasEnterSchool = false;
        boolean hasLeaveSchool = false;

        for (Permission p : permissions) {
            if (p instanceof SimplePermission sp) {
                if (sp.getAction() == Action.ENTER && sp.getLocationType() == LocationType.SCHOOL) {
                    hasEnterSchool = true;
                }
                if (sp.getAction() == Action.LEAVE && sp.getLocationType() == LocationType.SCHOOL) {
                    hasLeaveSchool = true;
                }
            }
        }

        assertTrue(hasEnterSchool, "Охранник должен иметь право входить в школу");
        assertTrue(hasLeaveSchool, "Охранник должен иметь право выходить из школы");

        assertFalse(permissions.stream()
                .anyMatch(p -> p instanceof ConditionalPermission), "У охранника не должно быть условных разрешений");
    }

    @Test
    void permissionsAreUnmodifiable() {
        Set<Permission> permissions = role.getPermissions();
        assertThrows(UnsupportedOperationException.class,
                () -> permissions.add(new SimplePermission(Action.ENTER, LocationType.SCHOOL)));
    }
}