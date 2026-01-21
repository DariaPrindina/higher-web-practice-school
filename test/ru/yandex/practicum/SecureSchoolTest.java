package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Location;
import ru.yandex.practicum.model.LocationType;

import static org.junit.jupiter.api.Assertions.*;

class SecureSchoolTest {

    private SecureStateImpl state;

    @BeforeEach
    void setUp() {
        state = new SecureStateImpl();
    }

    @Test
    void loginSuccessOwl() {
        String result = state.doAction("login", "owl", "7&");
        assertEquals("owl", result);
    }

    @Test
    void loginFailWrongPassword() {
        String result = state.doAction("login", "owl", "wrong");
        assertEquals("Неверный логин или пароль", result);
    }

    @Test
    void guardCannotEnterOwlCabinet() {
        state.doAction("login", "pooh", "1!");
        String result = state.doAction("enter", "owl");
        assertTrue(result.contains("Доступ запрещён"));
    }

    @Test
    void chiefCanEnterOwlCabinet() {
        state.doAction("login", "owl", "7&");
        String result = state.doAction("enter", "owl");
        assertTrue(result.contains("Вы вошли в OWL_CABINET"));
    }

    @Test
    void adminCanEnterOwlCabinet() {
        state.doAction("login", "robin", "**");
        String result = state.doAction("enter", "owl");
        assertTrue(result.contains("Вы вошли в OWL_CABINET"));
    }

    @Test
    void studentCannotEnterClassWithoutTeacher() {
        state.doAction("login", "roo", "5%");
        String result = state.doAction("enter", "A");
        assertTrue(result.contains("Доступ запрещён"));
    }

    @Test
    void parentCanWatchOnlyOwnChild() {
        state.doAction("login", "kanga", "??");
        String result = state.doAction("watch");
        assertTrue(result.contains("roo"));
    }

    @Test
    void teacherCanEditJournal() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("edit");
        assertEquals("Журнал обновлён", result);
    }

    @Test
    void studentSeesClassmatesGradesOnlyWithTeacher() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "A");

        state.doAction("login", "roo", "5%");
    }
}