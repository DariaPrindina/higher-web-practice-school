package ru.yandex.practicum.model;

import java.util.*;

public class Journal {
    private final Map<String, Map<String, Integer>> grades = new HashMap<>();

    public void addGrade(String studentLogin, String subject, int grade) {
        grades.computeIfAbsent(studentLogin, k -> new HashMap<>())
                .put(subject, grade);
    }

    public Map<String, Integer> getGradesForStudent(String studentLogin) {
        return Collections.unmodifiableMap(
                grades.getOrDefault(studentLogin, Collections.emptyMap()));
    }

    public Map<String, Map<String, Integer>> getAllGrades() {
        return Collections.unmodifiableMap(grades);
    }
}