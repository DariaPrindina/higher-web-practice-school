package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.Location;
import ru.yandex.practicum.model.LocationType;

import static org.junit.jupiter.api.Assertions.*;

class SecureSchoolTest {

    private SecureStateImpl state;

    @BeforeEach
    void setUp() {
        state = new SecureStateImpl();
    }

    // Аутентификация

    @Test
    void login_successful_returns_login_and_sets_currentUser() {
        String result = state.doAction("login", "owl", "7&");
        assertEquals("owl", result);
        assertNotNull(state.getCurrentUser());
        assertEquals("owl", state.getCurrentUser().getLogin());
    }

    @Test
    void login_wrong_password_returns_error_and_keeps_currentUser_null() {
        String result = state.doAction("login", "owl", "wrong");
        assertEquals("Неверный логин или пароль", result);
        assertNull(state.getCurrentUser());
    }

    @Test
    void login_unknown_user_returns_error() {
        String result = state.doAction("login", "nonexistent", "123");
        assertEquals("Неверный логин или пароль", result);
        assertNull(state.getCurrentUser());
    }

    @Test
    void login_too_few_arguments_returns_error() {
        String result = state.doAction("login", "owl");
        assertEquals("Недостаточно аргументов", result);
    }

    // Действия без авторизации

    @Test
    void doAction_without_login_returns_auth_error() {
        assertEquals("Сначала войдите в систему", state.doAction("enter", "school"));
        assertEquals("Сначала войдите в систему", state.doAction("watch"));
        assertEquals("Сначала войдите в систему", state.doAction("edit"));
        assertEquals("Сначала войдите в систему", state.doAction("leave", "school"));
    }

    // logout

    @Test
    void logout_clears_currentUser_and_occupied_locations() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        state.doAction("enter", "A");

        assertNotNull(state.getCurrentUser());
        assertFalse(state.getAreaList().isEmpty());

        String result = state.doAction("logout");
        assertEquals("Вы вышли из аккаунта", result);
        assertNull(state.getCurrentUser());
        assertTrue(state.getAreaList().isEmpty());
    }

    // enter / leave

    @Test
    void enter_valid_location_adds_to_occupied() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("enter", "school");
        assertEquals("Вы вошли в SCHOOL", result);
        assertEquals(1, state.getAreaList().size());
        assertTrue(state.getAreaList().contains(new Location(LocationType.SCHOOL)));
    }

    @Test
    void enter_unknown_location_returns_error() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("enter", "basement");
        assertEquals("Неизвестная команда: enter", result);
    }

    @Test
    void enter_not_allowed_location_returns_denied() {
        state.doAction("login", "pooh", "1!");
        String result = state.doAction("enter", "owl");
        assertTrue(result.contains("Доступ запрещён"));
        assertTrue(state.getAreaList().isEmpty());
    }

    @Test
    void leave_existing_location_removes_it() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        String result = state.doAction("leave", "school");
        assertEquals("Вы вышли из SCHOOL", result);
        assertTrue(state.getAreaList().isEmpty());
    }

    @Test
    void leave_non_existing_location_returns_message() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("leave", "school");
        assertEquals("Вы и так не там", result);
    }

    // watch

    @Test
    void watch_as_parent_shows_only_own_child_grades() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");

        state.doAction("login", "kanga", "??");
        state.doAction("enter", "school");
        String result = state.doAction("watch");

        assertTrue(result.contains("roo"));
        assertTrue(result.contains("Заклинания"));
        assertTrue(result.contains("5"));
    }

    @Test
    void watch_as_student_without_teacher_denied() {
        state.doAction("login", "roo", "5%");
        state.doAction("enter", "school");
        String result = state.doAction("watch");
        assertEquals("Нельзя смотреть журнал без учителя", result);
    }

    @Test
    void watch_as_teacher_shows_all_grades() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        String result = state.doAction("watch");
        assertTrue(result.contains("roo"));
        assertTrue(result.contains("Заклинания"));
        assertTrue(result.contains("5"));
    }

    // edit

    @Test
    void edit_as_teacher_adds_grade() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        String result = state.doAction("edit", "roo", "Математика", "4");
        assertEquals("Оценка добавлена: roo → Математика = 4", result);

        String watch = state.doAction("watch");
        assertTrue(watch.contains("Математика"));
        assertTrue(watch.contains("4"));
    }

    @Test
    void edit_as_guard_denied() {
        state.doAction("login", "pooh", "1!");
        String result = state.doAction("edit", "roo", "Математика", "4");
        assertEquals("Нет права редактировать журнал", result);
    }

    @Test
    void edit_invalid_format_returns_instruction() {
        state.doAction("login", "piglet", "2@");
        String result = state.doAction("edit", "roo", "Математика");
        assertTrue(result.contains("Формат: edit <ученик> <предмет> <оценка>"));
    }

    @Test
    void edit_invalid_grade_returns_error() {
        state.doAction("login", "piglet", "2@");
        assertTrue(state.doAction("edit", "roo", "Математика", "11").contains("от 1 до 5"));
        assertTrue(state.doAction("edit", "roo", "Математика", "abc").contains("от 1 до 5"));
    }

    // history

    @Test
    void history_contains_login_and_actions() {
        state.doAction("login", "piglet", "2@");
        state.doAction("enter", "school");
        state.doAction("watch");

        String result = state.doAction("history");
        assertTrue(result.contains("piglet вошёл в систему"));
        assertTrue(result.contains("piglet вошёл в SCHOOL"));
        assertTrue(result.contains("piglet посмотрел журнал"));
    }
}