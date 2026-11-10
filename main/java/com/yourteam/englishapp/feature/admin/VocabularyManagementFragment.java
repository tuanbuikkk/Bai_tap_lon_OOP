package com.yourteam.englishapp.feature.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yourteam.englishapp.data.local.entity.Vocabulary;
import com.yourteam.englishapp.databinding.FragmentVocabularyManagementBinding;
import com.yourteam.englishapp.feature.vocabulary.VocabularyViewModel;

public class VocabularyManagementFragment extends Fragment {
    private FragmentVocabularyManagementBinding binding;
    private VocabularyViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVocabularyManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(VocabularyViewModel.class);

        binding.btnSave.setOnClickListener(v -> {
            String word = binding.etWord.getText().toString().trim();
            String meaning = binding.etMeaning.getText().toString().trim();
            String topicIdStr = binding.etTopicId.getText().toString().trim();
            int topicId = topicIdStr.isEmpty() ? 1 : Integer.parseInt(topicIdStr);

            Vocabulary vocab = new Vocabulary();
            vocab.setTopicId(topicId);
            vocab.setWord(word);
            vocab.setMeaning(meaning);
            vocab.setPhonetic(binding.etPhonetic.getText().toString().trim());
            vocab.setPartOfSpeech(binding.etPartOfSpeech.getText().toString().trim());
            vocab.setExampleSentence(binding.etExample.getText().toString().trim());
            vocab.setExampleTranslation(binding.etTranslation.getText().toString().trim());

            vm.saveVocabulary(vocab);
        });

        vm.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                vm.clearMessage();
            }
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading ->
                binding.progress.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}