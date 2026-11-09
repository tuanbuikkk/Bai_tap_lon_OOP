package com.example.toeicapp.feature.flashcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.toeicapp.data.local.entity.FlashcardItem;
import com.example.toeicapp.databinding.FragmentFlashcardStudyBinding;

import java.util.List;

public class FlashcardStudyFragment extends Fragment {

    private FragmentFlashcardStudyBinding binding;
    private FlashcardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFlashcardStudyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FlashcardViewModel.class);

        Bundle args = getArguments();
        long flashcardId = args == null ? -1 : args.getLong("flashcardId", -1);
        if (flashcardId == -1) {
            NavHostFragment.findNavController(this).navigateUp();
            return;
        }

        if (savedInstanceState == null || viewModel.getItems().getValue() == null) {
            viewModel.selectFlashcard(flashcardId);
        }

        binding.cardFlash.setOnClickListener(v -> viewModel.flip());
        binding.btnKnown.setOnClickListener(v -> viewModel.answer(true));
        binding.btnUnknown.setOnClickListener(v -> viewModel.answer(false));
        binding.btnRestart.setOnClickListener(v -> viewModel.restart());
        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        viewModel.getItems().observe(getViewLifecycleOwner(), list -> render());
        viewModel.getIndex().observe(getViewLifecycleOwner(), index -> render());
        viewModel.getFlipped().observe(getViewLifecycleOwner(), flipped -> render());
    }

    private void render() {
        if (binding == null) {
            return;
        }

        List<FlashcardItem> list = viewModel.getItems().getValue();
        Integer index = viewModel.getIndex().getValue();
        boolean flipped = Boolean.TRUE.equals(viewModel.getFlipped().getValue());

        if (list == null || index == null) {
            showState(false, false, false);
            return;
        }

        if (list.isEmpty()) {
            showState(false, false, true);
            return;
        }

        if (index >= list.size()) {
            int learned = 0;
            for (FlashcardItem item : list) {
                if (item.learned) {
                    learned++;
                }
            }
            binding.tvResult.setText("Đã thuộc " + learned + "/" + list.size() + " từ");
            showState(false, true, false);
            return;
        }

        FlashcardItem item = list.get(index);
        binding.tvProgress.setText((index + 1) + "/" + list.size());
        binding.tvCard.setText(flipped ? item.meaning : item.word);
        binding.tvHint.setVisibility(flipped ? View.GONE : View.VISIBLE);
        binding.layoutAnswer.setVisibility(flipped ? View.VISIBLE : View.INVISIBLE);
        showState(true, false, false);
    }

    private void showState(boolean study, boolean finish, boolean empty) {
        binding.layoutStudy.setVisibility(study ? View.VISIBLE : View.GONE);
        binding.layoutFinish.setVisibility(finish ? View.VISIBLE : View.GONE);
        binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
