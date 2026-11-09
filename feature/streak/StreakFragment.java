package com.example.toeicapp.feature.streak;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.toeicapp.databinding.FragmentStreakBinding;
import com.example.toeicapp.session.UserSession;

import java.util.Calendar;

public class StreakFragment extends Fragment {

    private FragmentStreakBinding binding;
    private StreakViewModel viewModel;
    private CalendarAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStreakBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(StreakViewModel.class);
        viewModel.setUserId(UserSession.getInstance().getUserId());

        adapter = new CalendarAdapter();
        binding.rvCalendar.setLayoutManager(new GridLayoutManager(requireContext(), 7));
        binding.rvCalendar.setAdapter(adapter);

        binding.btnPrev.setOnClickListener(v -> viewModel.previousMonth());
        binding.btnNext.setOnClickListener(v -> viewModel.nextMonth());

        viewModel.getCurrentStreak().observe(getViewLifecycleOwner(), value ->
                binding.tvCurrentStreak.setText((value == null ? 0 : value) + " ngày"));

        viewModel.getLongestStreak().observe(getViewLifecycleOwner(), value ->
                binding.tvLongestStreak.setText((value == null ? 0 : value) + " ngày"));

        viewModel.getMonth().observe(getViewLifecycleOwner(), month -> {
            binding.tvMonth.setText("Tháng " + (month.get(Calendar.MONTH) + 1)
                    + "/" + month.get(Calendar.YEAR));
            adapter.setMonth(month);
        });

        viewModel.getStudiedDates().observe(getViewLifecycleOwner(), dates ->
                adapter.setStudiedDates(dates));

        viewModel.getCanGoNext().observe(getViewLifecycleOwner(), canGo -> {
            boolean enabled = Boolean.TRUE.equals(canGo);
            binding.btnNext.setEnabled(enabled);
            binding.btnNext.setAlpha(enabled ? 1f : 0.4f);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
        Calendar month = viewModel.getMonth().getValue();
        if (month != null) {
            adapter.setMonth(month);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
