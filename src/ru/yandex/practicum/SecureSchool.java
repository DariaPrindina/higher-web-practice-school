package ru.yandex.practicum;

import ru.yandex.practicum.impl.SecureStateImpl;

import java.util.Arrays;
import java.util.Scanner;

public class SecureSchool {

    private static final Scanner sc = new Scanner(System.in);
    private static String currentUser = null;
    private static final SecureState state = new SecureStateImpl();

    public static void main(String[] args) {
        while (true) {
            if (currentUser != null) {
                showState();
            }

            if (currentUser == null) {
                boolean success = doLogin();
                if (!success) {
                    return;
                }
                continue;
            }

            String cmd = showMenuAndRead();
            if (cmd == null || cmd.isBlank()) continue;

            if ("exit".equalsIgnoreCase(cmd)) {
                System.out.println("Программа завершена.");
                return;
            }

            String[] parts = cmd.trim().split("\\s+");
            String action = parts[0];
            String[] commandArgs = Arrays.copyOfRange(parts, 1, parts.length);

            String result = state.doAction(action, commandArgs);
            System.out.println(result);

            if ("logout".equalsIgnoreCase(action)) {
                currentUser = null;
            }
        }
    }

    private static boolean doLogin() {
        while (true) {
            System.out.print("Логин (или exit для выхода): ");
            String login = sc.nextLine().trim();

            if ("exit".equalsIgnoreCase(login)) {
                System.out.println("Программа завершена.");
                return false;
            }

            System.out.print("Пароль: ");
            String pass = sc.nextLine().trim();

            String res = state.doAction("login", login, pass);

            if (res.contains("Неверный") || res.contains("failed")) {
                System.out.println("Ошибка входа: " + res);
                System.out.println("Попробуйте снова.\n");
            } else {
                currentUser = res;
                System.out.println("Добро пожаловать, " + currentUser);
                return true;
            }
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