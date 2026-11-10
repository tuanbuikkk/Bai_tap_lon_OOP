package com.yourteam.englishapp.feature.vocabulary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yourteam.englishapp.databinding.FragmentVocabularyDetailBinding;

public class VocabularyDetailFragment extends Fragment {
    private FragmentVocabularyDetailBinding binding;
    private VocabularyViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVocabularyDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(VocabularyViewModel.class);

        int vocabId = getArguments() != null ? getArguments().getInt("vocabulary_id", 1) : 1;

        vm.getSelectedVocabulary().observe(getViewLifecycleOwner(), item -> {
            if (item != null) {
                binding.tvWord.setText(item.getWord());
                binding.tvPhonetic.setText(item.getPhonetic());
                binding.tvPartOfSpeech.setText(item.getPartOfSpeech());
                binding.tvMeaning.setText(item.getMeaning());
                binding.tvExample.setText(item.getExampleSentence());
                binding.tvTranslation.setText(item.getExampleTranslation());
            }
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading ->
                binding.progress.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE));

        vm.loadVocabularyDetail(vocabId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}