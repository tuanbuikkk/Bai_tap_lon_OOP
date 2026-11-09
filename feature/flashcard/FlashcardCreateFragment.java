package com.example.toeicapp.feature.flashcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.toeicapp.databinding.FragmentFlashcardCreateBinding;

public class FlashcardCreateFragment extends Fragment {

    private FragmentFlashcardCreateBinding binding;
    private FlashcardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFlashcardCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FlashcardViewModel.class);
        viewModel.clearSaved();
        viewModel.clearError();

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString();
            String description = binding.etDescription.getText().toString();
            viewModel.createFlashcard(name, description);
        });

        viewModel.getSaved().observe(getViewLifecycleOwner(), saved -> {
            if (Boolean.TRUE.equals(saved)) {
                viewModel.clearSaved();
                Toast.makeText(requireContext(), "Đã tạo bộ thẻ", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                viewModel.clearError();
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
