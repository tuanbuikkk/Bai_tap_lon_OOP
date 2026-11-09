package com.example.toeicapp.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.toeicapp.data.local.AppDatabase;
import com.example.toeicapp.data.local.entity.DailyActivity;
import com.example.toeicapp.repository.DailyActivityRepository;
import com.example.toeicapp.repository.DailyActivityRepositoryImpl;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class StreakService {

    private final DailyActivityRepository repository;

    public StreakService(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        repository = new DailyActivityRepositoryImpl(db.dailyActivityDao());
    }

    public static String dateKey(Calendar calendar) {
        return String.format(Locale.US, "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public LiveData<List<DailyActivity>> getActivities(long userId) {
        return repository.getByUser(userId);
    }

    public void recordStudy(long userId, int cards) {
        if (cards <= 0) {
            return;
        }
        repository.record(userId, dateKey(Calendar.getInstance()), cards);
    }

    public int calculateCurrentStreak(List<DailyActivity> activities) {
        Set<String> days = studiedDays(activities);
        Calendar day = Calendar.getInstance();

        if (!days.contains(dateKey(day))) {
            day.add(Calendar.DAY_OF_MONTH, -1);
        }

        int streak = 0;
        while (days.contains(dateKey(day))) {
            streak++;
            day.add(Calendar.DAY_OF_MONTH, -1);
        }
        return streak;
    }

    public int calculateLongestStreak(List<DailyActivity> activities) {
        TreeSet<String> days = studiedDays(activities);

        int longest = 0;
        int run = 0;
        String previous = null;
        for (String key : days) {
            if (previous != null && key.equals(nextDay(previous))) {
                run++;
            } else {
                run = 1;
            }
            longest = Math.max(longest, run);
            previous = key;
        }
        return longest;
    }

    private TreeSet<String> studiedDays(List<DailyActivity> activities) {
        TreeSet<String> days = new TreeSet<>();
        if (activities != null) {
            for (DailyActivity activity : activities) {
                if (activity.cardsStudied > 0) {
                    days.add(activity.date);
                }
            }
        }
        return days;
    }

    private String nextDay(String key) {
        int year = Integer.parseInt(key.substring(0, 4));
        int month = Integer.parseInt(key.substring(5, 7));
        int day = Integer.parseInt(key.substring(8, 10));

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return dateKey(calendar);
    }
}
