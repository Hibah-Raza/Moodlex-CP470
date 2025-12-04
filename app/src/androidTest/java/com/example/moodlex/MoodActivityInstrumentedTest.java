package com.example.moodlex;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Instrumented tests for MoodActivity.
 */
@RunWith(AndroidJUnit4.class)
public class MoodActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MoodActivity> activityRule =
            new ActivityTestRule<>(MoodActivity.class);

    @Test
    public void toolbar_isVisible() {
        onView(withId(R.id.mood_toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void menu_moodCheckIn_loadsFragment() {
        onView(withId(R.id.menu_mood_checkin)).perform(click());
        onView(withId(R.id.mood_fragmentC_container)).check(matches(isDisplayed()));
    }

    @Test
    public void menu_moodHistory_loadsFragment() {
        onView(withId(R.id.menu_mood_history)).perform(click());
        onView(withId(R.id.mood_fragmentC_container)).check(matches(isDisplayed()));
    }

    @Test
    public void menu_help_showsDialog() {
        onView(withId(R.id.menu_mood_help)).perform(click());
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
    }
}