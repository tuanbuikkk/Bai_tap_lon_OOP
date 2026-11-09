package com.example.toeicapp.feature.streak;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapp.databinding.ItemCalendarDayBinding;
import com.example.toeicapp.service.StreakService;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayHolder> {

    private static final int COLOR_STUDIED = 0xFFFF9800;
    private static final int COLOR_TODAY = 0xFF1976D2;
    private static final int COLOR_NORMAL = 0xFF212121;
    private static final int COLOR_FUTURE = 0xFFBDBDBD;
    private static final int COLOR_ON_STUDIED = Color.WHITE;

    private Calendar monthStart;
    private int offset;
    private int daysInMonth;
    private String todayKey = "";
    private Set<String> studiedDates = Collections.emptySet();

    public void setMonth(Calendar month) {
        Calendar start = (Calendar) month.clone();
        start.set(Calendar.DAY_OF_MONTH, 1);
        monthStart = start;
        offset = (start.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        daysInMonth = start.getActualMaximum(Calendar.DAY_OF_MONTH);
        todayKey = StreakService.dateKey(Calendar.getInstance());
        notifyDataSetChanged();
    }

    public void setStudiedDates(Set<String> dates) {
        studiedDates = dates == null ? Collections.<String>emptySet() : dates;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCalendarDayBinding binding = ItemCalendarDayBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DayHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position) {
        TextView text = holder.binding.tvDay;
        int day = position - offset + 1;

        if (day < 1) {
            text.setText("");
            text.setBackground(null);
            return;
        }

        Calendar date = (Calendar) monthStart.clone();
        date.set(Calendar.DAY_OF_MONTH, day);
        String key = StreakService.dateKey(date);

        boolean studied = studiedDates.contains(key);
        boolean today = key.equals(todayKey);
        boolean future = key.compareTo(todayKey) > 0;

        text.setText(String.valueOf(day));

        if (studied) {
            text.setTextColor(COLOR_ON_STUDIED);
        } else if (future) {
            text.setTextColor(COLOR_FUTURE);
        } else {
            text.setTextColor(COLOR_NORMAL);
        }

        if (studied || today) {
            int strokePx = today ? Math.round(2 * text.getResources().getDisplayMetrics().density) : 0;
            text.setBackground(circle(studied ? COLOR_STUDIED : Color.TRANSPARENT, strokePx));
        } else {
            text.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return monthStart == null ? 0 : offset + daysInMonth;
    }

    private static GradientDrawable circle(int fillColor, int strokePx) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(fillColor);
        if (strokePx > 0) {
            drawable.setStroke(strokePx, COLOR_TODAY);
        }
        return drawable;
    }

    static class DayHolder extends RecyclerView.ViewHolder {
        final ItemCalendarDayBinding binding;

        DayHolder(ItemCalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
