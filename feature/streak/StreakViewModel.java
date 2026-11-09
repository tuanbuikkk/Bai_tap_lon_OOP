package com.example.toeicapp.feature.streak;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.toeicapp.data.local.entity.DailyActivity;
import com.example.toeicapp.service.StreakService;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreakViewModel extends AndroidViewModel {

    private final StreakService streakService;

    private final MediatorLiveData<List<DailyActivity>> activities = new MediatorLiveData<>();
    private LiveData<List<DailyActivity>> activitiesSource;
    private Long currentUserId;

    private final MutableLiveData<Long> tick = new MutableLiveData<>(System.currentTimeMillis());
    private final MediatorLiveData<Integer> currentStreak = new MediatorLiveData<>();
    private final LiveData<Integer> longestStreak;
    private final LiveData<Set<String>> studiedDates;

    private final MutableLiveData<Calendar> month = new MutableLiveData<>(firstDayOfCurrentMonth());
    private final LiveData<Boolean> canGoNext;

    public StreakViewModel(@NonNull Application application) {
        super(application);
        streakService = new StreakService(application);

        currentStreak.addSource(activities, list -> updateCurrentStreak());
        currentStreak.addSource(tick, time -> updateCurrentStreak());

        longestStreak = Transformations.map(activities,
                list -> list == null ? 0 : streakService.calculateLongestStreak(list));

        studiedDates = Transformations.map(activities, list -> {
            Set<String> dates = new HashSet<>();
            if (list != null) {
                for (DailyActivity activity : list) {
                    if (activity.cardsStudied > 0) {
                        dates.add(activity.date);
                    }
                }
            }
            return dates;
        });

        canGoNext = Transformations.map(month,
                selected -> monthIndex(selected) < monthIndex(Calendar.getInstance()));
    }

    public void setUserId(long id) {
        if (currentUserId != null && currentUserId == id) {
            return;
        }
        currentUserId = id;
        if (activitiesSource != null) {
            activities.removeSource(activitiesSource);
        }
        activities.setValue(null);
        activitiesSource = streakService.getActivities(id);
        activities.addSource(activitiesSource, activities::setValue);
    }

    public void refresh() {
        tick.setValue(System.currentTimeMillis());
    }

    public LiveData<Integer> getCurrentStreak() {
        return currentStreak;
    }

    public LiveData<Integer> getLongestStreak() {
        return longestStreak;
    }

    public LiveData<Set<String>> getStudiedDates() {
        return studiedDates;
    }

    public LiveData<Calendar> getMonth() {
        return month;
    }

    public LiveData<Boolean> getCanGoNext() {
        return canGoNext;
    }

    public void previousMonth() {
        shiftMonth(-1);
    }

    public void nextMonth() {
        shiftMonth(1);
    }

    private void shiftMonth(int delta) {
        Calendar selected = month.getValue();
        if (selected == null) {
            return;
        }
        Calendar target = (Calendar) selected.clone();
        target.add(Calendar.MONTH, delta);
        if (monthIndex(target) > monthIndex(Calendar.getInstance())) {
            return;
        }
        month.setValue(target);
    }

    private void updateCurrentStreak() {
        List<DailyActivity> list = activities.getValue();
        currentStreak.setValue(list == null ? 0 : streakService.calculateCurrentStreak(list));
    }

    private static int monthIndex(Calendar calendar) {
        return calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
    }

    private static Calendar firstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
