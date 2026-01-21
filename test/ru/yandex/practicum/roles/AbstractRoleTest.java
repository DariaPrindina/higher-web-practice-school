package ru.yandex.practicum.roles;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AbstractRoleTest {

    private static class TestRole extends AbstractRole {
        public TestRole() {
            super("TestRole", Set.of(
                    new SimplePermission(Action.ENTER, LocationType.SCHOOL),
                    new SimplePermission(Action.WATCH, null)
            ));
        }
    }

    private static class EmptyRole extends AbstractRole {
        public EmptyRole() {
            super("Empty", Collections.emptySet());
        }
    }

    private static class NullSafeRole extends AbstractRole {
        public NullSafeRole() {
            super("NullSafe", null);
        }
    }

    private final TestRole role = new TestRole();

    @Test
    void getName_returnsConstructorValue() {
        assertEquals("TestRole", role.getName());
    }

    @Test
    void getPermissions_returnsUnmodifiableSet() {
        Set<Permission> permissions = role.getPermissions();
        assertEquals(2, permissions.size());

        assertThrows(UnsupportedOperationException.class,
                () -> permissions.add(new SimplePermission(Action.EDIT, null)));

        assertEquals(2, role.getPermissions().size());
    }

    @Test
    void constructor_acceptsEmptyPermissions() {
        EmptyRole emptyRole = new EmptyRole();
        assertTrue(emptyRole.getPermissions().isEmpty());
        assertEquals("Empty", emptyRole.getName());
    }

    @Test
    void toString_returnsRoleName() {
        assertEquals("TestRole", role.toString());
    }

    @Test
    void constructor_handlesNullPermissionsAsEmptySet() {
        AbstractRole nullSafeRole = new NullSafeRole();
        assertTrue(nullSafeRole.getPermissions().isEmpty());
        assertEquals("NullSafe", nullSafeRole.getName());
    }
}