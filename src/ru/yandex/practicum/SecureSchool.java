package ru.yandex.practicum;

import ru.yandex.practicum.impl.SecureStateImpl;

import java.util.Arrays;
import java.util.Scanner;

public class SecureSchool {

    private static final Scanner sc = new Scanner(System.in);
    private static String currentUser = null;
    private static final SecureState state = new SecureStateImpl();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showState();

            if (currentUser == null) {
                if (!doLogin()) {
                    continue;
                }
            } else {
                running = processCommand();
            }
        }
    }

    private static boolean processCommand() {
        String cmd = showMenuAndRead();
        if (cmd == null || cmd.isBlank()) return true;

        if ("exit".equalsIgnoreCase(cmd)) {
            System.out.println("Программа завершена.");
            return false;
        }

        String result = state.doAction(
                cmd.split("\\s+")[0],
                Arrays.copyOfRange(cmd.split("\\s+"), 1, cmd.split("\\s+").length)
        );
        System.out.println(result);

        return true;
    }

    private static boolean doLogin() {
        System.out.print("Логин (или exit для выхода): ");
        String login = sc.nextLine().trim();

        if ("exit".equalsIgnoreCase(login)) {
            System.out.println("Программа завершена.");
            System.exit(0);
        }

        System.out.print("Пароль: ");
        String pass = sc.nextLine().trim();

        String res = state.doAction("login", login, pass);
        if (res.contains("Неверный") || res.contains("failed")) {
            System.out.println("Ошибка входа: " + res);
            return false;
        } else {
            currentUser = res;
            System.out.println("Добро пожаловать, " + currentUser);
            return true;
        }
    }

    private static String showMenuAndRead() {
        System.out.println("\n=== Меню ===");
        System.out.println("enter school         — войти в школу");
        System.out.println("enter owl            — кабинет Совы");
        System.out.println("enter teachers       — учительская");
        System.out.println("enter A / B / C / D  — в класс");
        System.out.println("leave <место>        — выйти");
        System.out.println("watch                — посмотреть журнал");
        System.out.println("edit                 — редактировать журнал (если разрешено)");
        System.out.println("history              — показать историю действий");
        System.out.println("logout               — выйти из аккаунта");
        System.out.println("exit                 — завершить программу");
        System.out.print("\nВаша команда → ");
        return sc.nextLine().trim();
    }

    private static void showState() {
        System.out.println("\n───────────── LOCATIONS ─────────────");
        for (Object o : state.getAreaList()) {
            System.out.println("  " + o);
        }
        System.out.println("─────────────────────────────────────");
    }
}