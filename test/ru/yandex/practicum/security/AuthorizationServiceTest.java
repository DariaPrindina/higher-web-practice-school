package ru.yandex.practicum.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import ru.yandex.practicum.model.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationServiceTest {

    private AuthorizationService authz;
    private SecureStateImpl context;

    @BeforeEach
    void setUp() {
        context = new SecureStateImpl();
        authz = new AuthorizationService();
    }


    private User getUser(String login) {
        return new AuthService().authenticate(login, getPasswordFor(login));
    }

    private String getPasswordFor(String login) {
        return switch (login) {
            case "owl"   -> "7&";
            case "pooh"  -> "1!";
            case "piglet"-> "2@";
            case "roo"   -> "5%";
            case "kanga" -> "??";
            case "robin" -> "**";
            default      -> "unknown";
        };
    }


    @Test
    void isAllowed_nullUser_returnsFalse() {
        assertFalse(authz.isAllowed(null, Action.ENTER, new Location(LocationType.SCHOOL), context));
    }

    @Test
    void guard_canEnterSchool_butNotOwlCabinet() {
        User pooh = getUser("pooh");

        assertTrue(authz.isAllowed(pooh, Action.ENTER, new Location(LocationType.SCHOOL), context));
        assertTrue(authz.isAllowed(pooh, Action.LEAVE, new Location(LocationType.SCHOOL), context));

        assertFalse(authz.isAllowed(pooh, Action.ENTER, new Location(LocationType.OWL_CABINET), context));

        assertFalse(authz.isAllowed(pooh, Action.WATCH, null, context));
    }

    @Test
    void chief_canDoEverything_includingOwlCabinet() {
        User owl = getUser("owl");

        assertTrue(authz.isAllowed(owl, Action.ENTER, new Location(LocationType.SCHOOL), context));
        assertTrue(authz.isAllowed(owl, Action.ENTER, new Location(LocationType.OWL_CABINET), context));
        assertTrue(authz.isAllowed(owl, Action.ENTER, new Location(LocationType.TEACHERS_ROOM), context));
        assertTrue(authz.isAllowed(owl, Action.ENTER, new Location(LocationType.CLASS_A), context));

        assertTrue(authz.isAllowed(owl, Action.WATCH, null, context));
        assertTrue(authz.isAllowed(owl, Action.EDIT, null, context));
    }

    @Test
    void admin_canDoEverything_includingOwlCabinet() {
        User robin = getUser("robin");

        assertTrue(authz.isAllowed(robin, Action.ENTER, new Location(LocationType.OWL_CABINET), context));
        assertTrue(authz.isAllowed(robin, Action.EDIT, null, context));
    }

    @Test
    void student_canEnterSchool_butClassOnlyWithCondition() {
        User roo = getUser("roo");

        assertTrue(authz.isAllowed(roo, Action.ENTER, new Location(LocationType.SCHOOL), context));

        Location classA = new Location(LocationType.CLASS_A, "A");
        assertFalse(authz.isAllowed(roo, Action.ENTER, classA, context),
                "Ученик не должен входить в класс без условия учителя");

        assertFalse(authz.isAllowed(roo, Action.WATCH, null, context));
    }

    @Test
    void parent_canEnterSchool_andWatchOwnChild_onlyWhenTeacherPresent() {
        User kanga = getUser("kanga");

        assertTrue(authz.isAllowed(kanga, Action.ENTER, new Location(LocationType.SCHOOL), context));

        assertFalse(authz.isAllowed(kanga, Action.ENTER, new Location(LocationType.CLASS_A, "A"), context));

        assertFalse(authz.isAllowed(kanga, Action.WATCH, null, context),
                "Родитель не должен смотреть журнал без учителя");

        User teacher = getUser("piglet");
        context.doAction("login", "piglet", "2@");
        context.doAction("enter", "school");

        assertTrue(authz.isAllowed(kanga, Action.WATCH, null, context),
                "Родитель может смотреть журнал, когда учитель присутствует");
    }

    @Test
    void teacher_canEnterTeachersRoomAndClasses() {
        User piglet = getUser("piglet");

        assertTrue(authz.isAllowed(piglet, Action.ENTER, new Location(LocationType.TEACHERS_ROOM), context));
        assertTrue(authz.isAllowed(piglet, Action.ENTER, new Location(LocationType.CLASS_A), context));
        assertTrue(authz.isAllowed(piglet, Action.WATCH, null, context));
        assertTrue(authz.isAllowed(piglet, Action.EDIT, null, context));

        assertFalse(authz.isAllowed(piglet, Action.ENTER, new Location(LocationType.OWL_CABINET), context));
    }

    @Test
    void isAllowed_nullLocation_forWatch_shouldBeAllowedForTeacher() {
        User teacher = getUser("piglet");
        assertTrue(authz.isAllowed(teacher, Action.WATCH, null, context));
    }
}