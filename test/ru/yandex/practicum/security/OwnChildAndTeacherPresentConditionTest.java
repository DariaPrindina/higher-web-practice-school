package ru.yandex.practicum.security;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.roles.ParentRole;

import static org.junit.jupiter.api.Assertions.*;

class OwnChildAndTeacherPresentConditionTest {
    @Test
    void ownChildAndTeacherPresentCondition_returnsFalse_whenNoChild() {
        User parentWithoutChild = new User("parent", "pass", new ParentRole(), null);
        SecureStateImpl context = new SecureStateImpl();

        Condition condition = new OwnChildAndTeacherPresentCondition();
        boolean satisfied = condition.isSatisfied(parentWithoutChild, null, context);

        assertFalse(satisfied, "Если у родителя нет ребёнка → условие false");
    }

    @Test
    void ownChildAndTeacherPresentCondition_returnsTrue_whenChildExistsAndTeacherPresent() {
        User parent = new User("kanga", "??", new ParentRole(), "roo");
        SecureStateImpl context = new SecureStateImpl();

        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "school");

        Condition condition = new OwnChildAndTeacherPresentCondition();
        boolean satisfied = condition.isSatisfied(parent, null, context);

        assertTrue(satisfied, "Если ребёнок есть и учитель присутствует → true");
    }

    @Test
    void ownChildAndTeacherPresentCondition_returnsFalse_whenNoTeacher() {
        User parent = new User("kanga", "??", new ParentRole(), "roo");
        SecureStateImpl context = new SecureStateImpl();

        Condition condition = new OwnChildAndTeacherPresentCondition();
        boolean satisfied = condition.isSatisfied(parent, null, context);

        assertFalse(satisfied, "Если учителя нет → false, даже если ребёнок есть");
    }

}