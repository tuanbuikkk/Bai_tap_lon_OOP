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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.toeicapp.R;
import com.example.toeicapp.data.local.entity.Flashcard;
import com.example.toeicapp.databinding.FragmentFlashcardListBinding;
import com.example.toeicapp.session.UserSession;

public class FlashcardListFragment extends Fragment {

    private FragmentFlashcardListBinding binding;
    private FlashcardViewModel viewModel;
    private FlashcardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFlashcardListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FlashcardViewModel.class);
        viewModel.setUserId(UserSession.getInstance().getUserId());

        adapter = new FlashcardAdapter(new FlashcardAdapter.OnFlashcardListener() {
            @Override
            public void onClick(Flashcard flashcard) {
                Bundle args = new Bundle();
                args.putLong("flashcardId", flashcard.id);
                NavHostFragment.findNavController(FlashcardListFragment.this)
                        .navigate(R.id.flashcardStudyFragment, args);
            }

            @Override
            public void onDelete(Flashcard flashcard) {
                viewModel.deleteFlashcard(flashcard);
            }
        });

        binding.rvFlashcards.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFlashcards.setAdapter(adapter);

        binding.fabAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.flashcardCreateFragment));

        viewModel.getFlashcards().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
