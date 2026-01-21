package ru.yandex.practicum.impl;

import ru.yandex.practicum.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.roles.ParentRole;
import ru.yandex.practicum.roles.StudentRole;
import ru.yandex.practicum.roles.TeacherRole;
import ru.yandex.practicum.security.*;

import java.time.LocalDateTime;
import java.util.*;

public class SecureStateImpl implements SecureState, SecureContext {

    private User currentUser = null;
    private final AuthService auth = new AuthService();
    private final AuthorizationService authz = new AuthorizationService();

    private final List<String> history = new ArrayList<>();
    public final Set<Location> occupied = new HashSet<>();

    private final Journal journal = new Journal();

    public SecureStateImpl() {
        journal.addGrade("roo", "Заклинания", 5);
        journal.addGrade("roo", "ЗОТИ", 3);
        journal.addGrade("roo", "Травология", 4);
    }

    @Override
    public List<Object> getAreaList() {
        return new ArrayList<>(occupied);
    }

    @Override
    public List<Object> getActionHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public String doAction(String action, String... args) {
        if ("login".equalsIgnoreCase(action)) {
            if (args.length < 2) return "Недостаточно аргументов";
            currentUser = auth.authenticate(args[0], args[1]);
            if (currentUser == null) return "Неверный логин или пароль";
            log(currentUser.getLogin() + " вошёл в систему");
            return currentUser.getLogin();
        }

        if (currentUser == null) return "Сначала войдите в систему";

        return switch (action.toLowerCase()) {
            case "enter" -> cmdEnter(args);
            case "leave" -> cmdLeave(args);
            case "watch" -> cmdWatch(args);
            case "history" -> {
                StringBuilder sb = new StringBuilder("История действий:\n");
                history.forEach(sb::append);
                yield sb.toString();
            }
            case "edit" -> cmdEdit(args);
            case "logout" -> {
                if (currentUser != null) {
                    occupied.clear();
                    log(currentUser.getLogin() + " вышел из системы");
                    currentUser = null;
                }
                yield "Вы вышли из аккаунта";
            }
            default -> "Неизвестная команда: " + action;
        };
    }

    private String cmdEnter(String[] args) {
        if (args.length == 0) return "Укажите куда войти";
        Location loc = parseLocation(args);

        if (loc == null) {
            return "Неизвестная команда: enter";
        }

        if (!authz.isAllowed(currentUser, Action.ENTER, loc, this)) {
            return "Доступ запрещён → " + loc;
        }

        occupied.add(loc);
        log(currentUser.getLogin() + " вошёл в " + loc);
        return "Вы вошли в " + loc;
    }

    private String cmdLeave(String[] args) {
        if (args.length == 0) return "Укажите откуда выйти";
        Location loc = parseLocation(args);

        if (occupied.remove(loc)) {
            log(currentUser.getLogin() + " вышел из " + loc);
            return "Вы вышли из " + loc;
        }
        return "Вы и так не там";
    }

    private String cmdWatch(String[] args) {
        if (!authz.isAllowed(currentUser, Action.WATCH, null, this)) {
            return "Нет права смотреть журнал";
        }

        StringBuilder sb = new StringBuilder("Журнал оценок:\n");

        if (currentUser.getRole() instanceof StudentRole) {
            if (!new TeacherPresentCondition().isSatisfied(currentUser, null, this)) {
                return "Нельзя смотреть журнал без учителя";
            }
            String studentLogin = currentUser.getLogin();
            Map<String, Integer> grades = journal.getGradesForStudent(studentLogin);
            if (grades.isEmpty()) {
                sb.append("У вас пока нет оценок.\n");
            } else {
                grades.forEach((subject, grade) -> sb.append(subject).append(" → ").append(grade).append("\n"));
            }
        } else if (currentUser.getRole() instanceof ParentRole) {
            if (!new TeacherPresentCondition().isSatisfied(currentUser, null, this)) {
                return "Нельзя смотреть журнал без учителя";
            }
            String child = currentUser.getChildLogin();
            if (child == null) return "У вас нет ребёнка в школе";
            sb.append("Оценки ").append(child).append(":\n");
            Map<String, Integer> grades = journal.getGradesForStudent(child);
            if (grades.isEmpty()) {
                sb.append("У вашего ребёнка пока нет оценок.\n");
            } else {
                grades.forEach((subject, grade) ->
                        sb.append(subject).append(" → ").append(grade).append("\n"));
            }
        } else {
            journal.getAllGrades().forEach((student, subjects) -> {
                sb.append("Ученик: ").append(student).append("\n");
                subjects.forEach((subject, grade) ->
                        sb.append("  ").append(subject).append(" → ").append(grade).append("\n"));
            });
        }

        log(currentUser.getLogin() + " посмотрел журнал");
        return sb.toString();
    }

    private String cmdEdit(String[] args) {
        if (!authz.isAllowed(currentUser, Action.EDIT, null, this)) {
            return "Нет права редактировать журнал";
        }

        if (args.length < 3) {
            return "Формат: edit <ученик> <предмет> <оценка>\nПример: edit roo Заклинания 4";
        }

        String student = args[0];
        String subject = args[1];
        int grade;
        try {
            grade = Integer.parseInt(args[2]);
            if (grade < 1 || grade > 5) throw new NumberFormatException();
        } catch (Exception e) {
            return "Оценка должна быть числом от 1 до 5";
        }

        journal.addGrade(student, subject, grade);
        log(currentUser.getLogin() + " поставил оценку " + grade + " по " + subject + " ученику " + student);
        return "Оценка добавлена: " + student + " → " + subject + " = " + grade;
    }

    private Location parseLocation(String[] args) {
        String target = args[0].toUpperCase();
        return switch (target) {
            case "SCHOOL" -> new Location(LocationType.SCHOOL);
            case "OWL", "CABIN" -> new Location(LocationType.OWL_CABINET);
            case "TEACHERS", "ROOM" -> new Location(LocationType.TEACHERS_ROOM);
            case "A" -> new Location(LocationType.CLASS_A, "A");
            case "B" -> new Location(LocationType.CLASS_B, "B");
            case "C" -> new Location(LocationType.CLASS_C, "C");
            case "D" -> new Location(LocationType.CLASS_D, "D");
            default -> null;
        };
    }

    private void log(String msg) {
        history.add(LocalDateTime.now() + "  " + msg);
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isTeacherPresent(Location location) {
        if (location == null) {
            return occupied.contains(new Location(LocationType.SCHOOL)) ||
                    occupied.stream().anyMatch(l -> l.getType().name().startsWith("CLASS_"));
        }

        return occupied.contains(location);
    }

    @Override
    public Journal getJournal() {
        return journal;
    }
}