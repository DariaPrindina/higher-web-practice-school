package ru.yandex.practicum.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JournalTest {

    private Journal journal;

    @BeforeEach
    void setUp() {
        journal = new Journal();
    }

    @Test
    void addGrade_addsNewGrade() {
        journal.addGrade("roo", "Заклинания", 5);
        Map<String, Integer> grades = journal.getGradesForStudent("roo");
        assertEquals(5, grades.get("Заклинания"));
    }

    @Test
    void addGrade_overwritesExistingGrade() {
        journal.addGrade("roo", "Заклинания", 3);
        journal.addGrade("roo", "Заклинания", 5);
        assertEquals(5, journal.getGradesForStudent("roo").get("Заклинания"));
    }

    @Test
    void getGradesForStudent_returnsUnmodifiableMap() {
        journal.addGrade("roo", "ЗОТИ", 4);
        Map<String, Integer> grades = journal.getGradesForStudent("roo");
        assertThrows(UnsupportedOperationException.class, () -> grades.put("new", 10));
    }

    @Test
    void getGradesForUnknownStudent_returnsEmptyMap() {
        Map<String, Integer> grades = journal.getGradesForStudent("unknown");
        assertTrue(grades.isEmpty());
    }

    @Test
    void getAllGrades_returnsUnmodifiableMap() {
        journal.addGrade("roo", "Травология", 4);
        Map<String, Map<String, Integer>> all = journal.getAllGrades();
        assertThrows(UnsupportedOperationException.class, () -> all.put("new", Map.of()));
    }
}