package com.yourteam.englishapp.feature.vocabulary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yourteam.englishapp.databinding.FragmentVocabularyBinding;

public class VocabularyFragment extends Fragment {
    private FragmentVocabularyBinding binding;
    private VocabularyViewModel vm;
    private VocabularyAdapter adapter;
    private int currentTopicId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVocabularyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(VocabularyViewModel.class);

        if (getArguments() != null) {
            currentTopicId = getArguments().getInt("topic_id", 1);
            String topicName = getArguments().getString("topic_name", "Từ vựng");
            binding.tvTitle.setText(topicName);
        }

        adapter = new VocabularyAdapter(vocabulary -> {
            Bundle bundle = new Bundle();
            bundle.putInt("vocabulary_id", vocabulary.getId());
            NavHostFragment.findNavController(this).navigate(
                    com.yourteam.englishapp.R.id.vocabularyDetailFragment, bundle);
        });

        binding.recyclerVocabulary.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerVocabulary.setAdapter(adapter);

        vm.getVocabularies().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            binding.tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading ->
                binding.progress.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE));

        vm.loadVocabulariesByTopic(currentTopicId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}