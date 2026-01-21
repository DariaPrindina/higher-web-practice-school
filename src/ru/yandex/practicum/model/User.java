package ru.yandex.practicum.model;

public class User {
    private final String login;
    private final String password;
    private final Role role;
    private final String childLogin;

    public User(String login, String password, Role role) {
        this(login, password, role, null);
    }

    public User(String login, String password, Role role, String childLogin) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.childLogin = childLogin;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getChildLogin() {
        return childLogin;
    }

    @Override
    public String toString() {
        return login + " [" + role.getName() + "]";
    }
}