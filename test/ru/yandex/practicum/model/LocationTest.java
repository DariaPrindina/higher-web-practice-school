package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void equals_and_hashCode_sameTypeAndDetail_areEqual() {
        Location loc1 = new Location(LocationType.CLASS_A, "A");
        Location loc2 = new Location(LocationType.CLASS_A, "A");
        assertEquals(loc1, loc2);
        assertEquals(loc1.hashCode(), loc2.hashCode());
    }

    @Test
    void equals_differentDetail_notEqual() {
        Location loc1 = new Location(LocationType.CLASS_A, "A");
        Location loc2 = new Location(LocationType.CLASS_A, "B");
        assertNotEquals(loc1, loc2);
    }

    @Test
    void equals_differentType_notEqual() {
        Location loc1 = new Location(LocationType.CLASS_A);
        Location loc2 = new Location(LocationType.CLASS_B);
        assertNotEquals(loc1, loc2);
    }

    @Test
    void equals_nullDetail_equalsOnlyIfBothNull() {
        Location loc1 = new Location(LocationType.SCHOOL);
        Location loc2 = new Location(LocationType.SCHOOL);
        assertEquals(loc1, loc2);

        Location loc3 = new Location(LocationType.SCHOOL, null);
        assertEquals(loc1, loc3);
    }

    @Test
    void toString_correctFormat() {
        assertEquals("SCHOOL", new Location(LocationType.SCHOOL).toString());
        assertEquals("CLASS_A (A)", new Location(LocationType.CLASS_A, "A").toString());
    }
}