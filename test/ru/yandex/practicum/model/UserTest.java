package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.roles.AdminRole;
import ru.yandex.practicum.roles.StudentRole;
import ru.yandex.practicum.roles.ParentRole;
import ru.yandex.practicum.roles.TeacherRole;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_correctlySetsFields() {
        User user = new User("roo", "5%", new StudentRole(), null);

        assertEquals("roo", user.getLogin());
        assertEquals("5%", user.getPassword());
        assertInstanceOf(StudentRole.class, user.getRole());
        assertNull(user.getChildLogin());
    }

    @Test
    void parentConstructor_setsChildLogin() {
        User parent = new User("kanga", "??", new ParentRole(), "roo");

        assertEquals("kanga", parent.getLogin());
        assertEquals("??", parent.getPassword());
        assertInstanceOf(ParentRole.class, parent.getRole());
        assertEquals("roo", parent.getChildLogin());
    }

    @Test
    void toString_returnsCorrectFormat() {
        User user = new User("piglet", "2@", new TeacherRole());
        assertEquals("piglet [Teacher]", user.toString());

        User parent = new User("kanga", "??", new ParentRole(), "roo");
        assertEquals("kanga [Parent]", parent.toString());
    }

    @Test
    void getters_returnCorrectValues() {
        User user = new User("robin", "**", new AdminRole(), null);

        assertEquals("robin", user.getLogin());
        assertEquals("**", user.getPassword());
        assertInstanceOf(AdminRole.class, user.getRole());
        assertNull(user.getChildLogin());
    }
}