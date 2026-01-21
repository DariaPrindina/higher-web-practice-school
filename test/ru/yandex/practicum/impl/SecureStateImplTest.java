package ru.yandex.practicum.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.roles.*;
import ru.yandex.practicum.security.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecureStateImplTest {

    private SecureStateImpl state;

    @BeforeEach
    void setUp() {
        state = new SecureStateImpl();
    }

    // Аутентификация

    @Test
    void login_successful_setsCurrentUser() {
        String result = state.doAction("login", "roo", "5%");
        assertEquals("roo", result);
        assertNotNull(state.getCurrentUser());
        assertEquals("roo", state.getCurrentUser().getLogin());
        assertInstanceOf(StudentRole.class, state.getCurrentUser().getRole());
    }

    @Test
    void login_wrongPassword_returnsError() {
        String result = state.doAction("login", "roo", "wrong");
        assertEquals("Неверный логин или пароль", result);
        assertNull(state.getCurrentUser());
    }

    @Test
    void actions_withoutLogin_returnAuthError() {
        assertEquals("Сначала войдите в систему", state.doAction("enter", "school"));
        assertEquals("Сначала войдите в систему", state.doAction("watch"));
    }

    // Вход / выход из локаций

    @Test
    void guard_canEnterAndLeaveSchool() {
        state.doAction("login", "pooh", "1!");
        assertEquals("Вы вошли в SCHOOL", state.doAction("enter", "school"));
        assertTrue(state.getAreaList().contains(new Location(LocationType.SCHOOL)));

        assertEquals("Вы вышли из SCHOOL", state.doAction("leave", "school"));
        assertFalse(state.getAreaList().contains(new Location(LocationType.SCHOOL)));
    }

    @Test
    void student_cannotEnterClassWithoutTeacher() {
        state.doAction("login", "roo", "5%");
        assertEquals("Доступ запрещён → CLASS_A (A)",
                state.doAction("enter", "A"));
    }

    @Test
    void teacher_canEnterAnyClass() {
        state.doAction("login", "piglet", "2@");
        assertEquals("Вы вошли в CLASS_A (A)", state.doAction("enter", "A"));
        assertTrue(state.getAreaList().contains(new Location(LocationType.CLASS_A, "A")));
    }

    // Просмотр журнала

    @Test
    void student_cannotWatchJournalWithoutTeacher() {
        state.doAction("login", "roo", "5%");
        String result = state.doAction("watch");
        assertEquals("Нельзя смотреть журнал без учителя", result);
    }

    @Test
    void student_canWatchOnlyOwnGradesWhenTeacherPresent() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");

        state.doAction("login", "roo", "5%");
        state.doAction("enter", "school");

        String result = state.doAction("watch");
        assertTrue(result.contains("Заклинания → 5"));
        assertTrue(result.contains("ЗОТИ → 3"));
        assertTrue(result.contains("Травология → 4"));
        assertFalse(result.contains("Другие ученики"));
    }

    @Test
    void parent_canWatchOnlyChildGrades_whenTeacherPresent() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");

        state.doAction("login", "kanga", "??");
        String result = state.doAction("watch");

        assertTrue(result.contains("roo"));
        assertTrue(result.contains("Заклинания → 5"));
        assertFalse(result.contains("piglet"));
    }

    @Test
    void parent_cannotWatchJournalWithoutTeacher() {
        state.doAction("login", "kanga", "??");
        String result = state.doAction("watch");
        assertEquals("Нельзя смотреть журнал без учителя", result);
    }

    @Test
    void teacher_canWatchAllGrades() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("watch");
        assertTrue(result.contains("roo"));
        assertTrue(result.contains("Заклинания → 5"));
    }

    // Редактирование журнала

    @Test
    void teacher_canEditJournal() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("edit", "roo", "Математика", "4");
        assertEquals("Оценка добавлена: roo → Математика = 4", result);

        state.doAction("watch");
        String watch = state.doAction("watch");
        assertTrue(watch.contains("Математика → 4"));
    }

    @Test
    void guard_cannotEditJournal() {
        state.doAction("login", "pooh", "1!");
        String result = state.doAction("edit", "roo", "Математика", "5");
        assertEquals("Нет права редактировать журнал", result);
    }

    // История действий

    @Test
    void history_containsLoginAndActions() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        state.doAction("watch");

        String history = state.doAction("history");
        assertTrue(history.contains("piglet вошёл в систему"));
        assertTrue(history.contains("piglet вошёл в SCHOOL"));
        assertTrue(history.contains("piglet посмотрел журнал"));
    }

    @Test
    void logout_clearsOccupiedAndCurrentUser() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        state.doAction("enter", "A");

        assertFalse(state.getAreaList().isEmpty());
        assertNotNull(state.getCurrentUser());

        state.doAction("logout");

        assertTrue(state.getAreaList().isEmpty());
        assertNull(state.getCurrentUser());
    }

    @Test
    void occupied_keepsTeacherPresenceAfterStudentLogin() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");

        state.doAction("login", "roo", "5%");
        state.doAction("enter", "school");

        String result = state.doAction("watch");
        assertTrue(result.contains("Заклинания → 5"));
    }
}