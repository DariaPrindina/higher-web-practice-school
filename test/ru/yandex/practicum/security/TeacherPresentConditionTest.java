package ru.yandex.practicum.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.roles.*;

import static org.junit.jupiter.api.Assertions.*;

class TeacherPresentConditionTest {

    private TeacherPresentCondition condition;
    private SecureStateImpl context;
    private User teacher;
    private User student;

    @BeforeEach
    void setUp() {
        condition = new TeacherPresentCondition();
        context = new SecureStateImpl();

        teacher = new User("piglet", "2@", new TeacherRole());
        student = new User("roo", "5%", new StudentRole());
    }

    @Test
    void isSatisfied_returnsTrue_whenTeacherIsPresentInLocation() {
        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "school");
        context.doAction("enter", "A");

        boolean satisfied = condition.isSatisfied(student, new Location(LocationType.CLASS_A, "A"), context);
        assertTrue(satisfied, "Учитель должен быть засчитан как присутствующий в классе");
    }

    @Test
    void isSatisfied_returnsFalse_whenTeacherNotInLocation() {
        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "school");

        boolean satisfied = condition.isSatisfied(student, new Location(LocationType.CLASS_A, "A"), context);
        assertFalse(satisfied, "Учитель не в классе → условие не выполнено");
    }

    @Test
    void isSatisfied_returnsFalse_whenNoCurrentUser() {
        boolean satisfied = condition.isSatisfied(student, new Location(LocationType.CLASS_A, "A"), context);
        assertFalse(satisfied);
    }

    @Test
    void isSatisfied_returnsFalse_whenCurrentUserIsNotTeacher() {
        context.doAction("login", "roo", "5%");
        context.doAction("enter", "A");

        boolean satisfied = condition.isSatisfied(student, new Location(LocationType.CLASS_A, "A"), context);
        assertFalse(satisfied, "Текущий пользователь — не учитель");
    }

    @Test
    void isSatisfied_returnsFalse_whenWrongLocation() {
        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "school");
        context.doAction("enter", "A");

        boolean satisfied = condition.isSatisfied(student, new Location(LocationType.CLASS_B, "B"), context);
        assertFalse(satisfied, "Учитель в классе A, а проверяем класс B");
    }

    @Test
    void isSatisfied_returnsFalse_whenContextIsNullOrWrongType() {
        boolean satisfied = condition.isSatisfied(student, null, null);
        assertFalse(satisfied);
    }
}