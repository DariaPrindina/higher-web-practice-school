package ru.yandex.practicum.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.roles.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void authenticate_correctCredentials_returnsUserWithCorrectRole() {
        User owl = authService.authenticate("owl", "7&");
        assertNotNull(owl);
        assertEquals("owl", owl.getLogin());
        assertInstanceOf(ChiefRole.class, owl.getRole());
        assertNull(owl.getChildLogin());

        User pooh = authService.authenticate("pooh", "1!");
        assertNotNull(pooh);
        assertInstanceOf(GuardRole.class, pooh.getRole());

        User piglet = authService.authenticate("piglet", "2@");
        assertNotNull(piglet);
        assertInstanceOf(TeacherRole.class, piglet.getRole());
    }

    @Test
    void authenticate_parent_hasChildLogin() {
        User kanga = authService.authenticate("kanga", "??");
        assertNotNull(kanga);
        assertEquals("kanga", kanga.getLogin());
        assertInstanceOf(ParentRole.class, kanga.getRole());
        assertEquals("roo", kanga.getChildLogin());
    }

    @Test
    void authenticate_wrongPassword_returnsNull() {
        User user = authService.authenticate("owl", "wrongpassword");
        assertNull(user, "Должен вернуть null при неверном пароле");
    }

    @Test
    void authenticate_nonExistentLogin_returnsNull() {
        User user = authService.authenticate("unknown_user", "123");
        assertNull(user, "Несуществующий логин должен вернуть null");
    }

    @Test
    void authenticate_caseSensitiveLogin() {
        User owlLower = authService.authenticate("OWL", "7&");
        assertNull(owlLower, "Логин должен быть чувствителен к регистру");

        User owlCorrect = authService.authenticate("owl", "7&");
        assertNotNull(owlCorrect);
    }

    @Test
    void authenticate_allUsersCanBeLoaded() {
        String[] expectedLogins = {"pooh", "piglet", "tigger", "eeyore", "kanga", "roo", "rabbit", "owl", "robin"};

        for (String login : expectedLogins) {
            String password = getPasswordFor(login);
            User user = authService.authenticate(login, password);
            assertNotNull(user, "Пользователь " + login + " должен успешно аутентифицироваться");
            assertEquals(login, user.getLogin());
        }
    }

    private String getPasswordFor(String login) {
        return switch (login) {
            case "pooh"   -> "1!";
            case "piglet" -> "2@";
            case "tigger" -> "3#";
            case "eeyore" -> "4$";
            case "kanga"  -> "??";
            case "roo"    -> "5%";
            case "rabbit" -> "6^";
            case "owl"    -> "7&";
            case "robin"  -> "**";
            default       -> "unknown";
        };
    }
}