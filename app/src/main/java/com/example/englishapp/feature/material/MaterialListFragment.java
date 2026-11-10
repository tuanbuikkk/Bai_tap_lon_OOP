package com.example.englishapp.feature.material;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.englishapp.R;
import com.example.englishapp.databinding.FragmentMaterialListBinding;
import com.google.android.material.chip.Chip;

public class MaterialListFragment extends Fragment {
    // {nhãn hiển thị, giá trị lọc} - giá trị rỗng nghĩa là tất cả
    private static final String[][] CATEGORIES = {
            {"Tất cả", ""},
            {"Part 1", "Part 1"}, {"Part 2", "Part 2"}, {"Part 3", "Part 3"},
            {"Part 4", "Part 4"}, {"Part 5", "Part 5"}, {"Part 6", "Part 6"},
            {"Part 7", "Part 7"},
            {"Ngữ pháp", "Ngữ pháp"}, {"Từ vựng", "Từ vựng"}
    };

    private FragmentMaterialListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMaterialListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(MaterialViewModel.class);

        MaterialAdapter adapter = new MaterialAdapter(item ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                MaterialDetailFragment.newInstance(item.getId()))
                        .addToBackStack(null)
                        .commit());
        binding.rvMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMaterials.setAdapter(adapter);

        // Tạo các chip danh mục, chọn sẵn chip đang lọc
        String current = viewModel.getCategory();
        for (String[] c : CATEGORIES) {
            Chip chip = new Chip(requireContext());
            chip.setText(c[0]);
            chip.setTag(c[1]);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            binding.chipGroup.addView(chip);
            if (c[1].equals(current)) chip.setChecked(true);
        }
        binding.chipGroup.setOnCheckedStateChangeListener((group, ids) -> {
            if (ids.isEmpty()) return;
            Chip chip = group.findViewById(ids.get(0));
            if (chip != null) viewModel.setCategory((String) chip.getTag());
        });

        viewModel.getMaterials().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
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