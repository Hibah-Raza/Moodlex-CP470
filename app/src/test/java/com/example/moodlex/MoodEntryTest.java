package com.example.moodlex;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the MoodEntry data model.
 */
public class MoodEntryTest {

    @Test
    public void moodEntry_isCreatedCorrectly() {
        String emoji = "😊";
        String note = "Feeling good today!";
        long time = System.currentTimeMillis();

        MoodEntry entry = new MoodEntry(emoji, note, time);

        assertEquals("😊", entry.getMoodEmoji());
        assertEquals("Feeling good today!", entry.getNote());
        assertEquals(time, entry.getTimestamp());
    }

    @Test
    public void moodEntry_handlesNullValues() {
        long time = System.currentTimeMillis();

        MoodEntry entry = new MoodEntry(null, null, time);

        assertNull(entry.getMoodEmoji());
        assertNull(entry.getNote());
        assertEquals(time, entry.getTimestamp());
    }
}