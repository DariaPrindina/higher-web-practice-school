package ru.yandex.practicum.security;

import ru.yandex.practicum.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.roles.*;

import java.util.*;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public AuthService() {
        initUsers();
    }

    private void initUsers() {
        String[] logins    = SecureDatabase.USERS;
        String[] passes    = SecureDatabase.PASSWORDS;
        String[][] roles   = SecureDatabase.USER_ROLES;

        for (int i = 0; i < logins.length; i++) {
            String login = logins[i];
            String pass  = passes[i];
            String roleName = roles[i][0];

            Role role = switch (roleName) {
                case "ADMIN"   -> new AdminRole();
                case "CHIEF"   -> new ChiefRole();
                case "TEACHER" -> new TeacherRole();
                case "GUARD"   -> new GuardRole();
                case "STUDENT" -> new StudentRole();
                case "PARENT"  -> new ParentRole();
                default        -> throw new IllegalArgumentException("Unknown role: " + roleName);
            };

            String child = "PARENT".equals(roleName) ? "roo" : null;

            users.put(login, new User(login, pass, role, child));
        }
    }

    public User authenticate(String login, String password) {
        User u = users.get(login);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
}