package com.example.englishapp.feature.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.englishapp.data.local.entity.StudyMaterial;
import com.example.englishapp.databinding.DialogMaterialEditBinding;
import com.example.englishapp.databinding.FragmentMaterialManagementBinding;
import com.example.englishapp.feature.material.MaterialAdapter;
import com.example.englishapp.feature.material.MaterialViewModel;

public class MaterialManagementFragment extends Fragment {
    private static final String[] TYPE_LABELS = {"Bài đọc (văn bản)", "Liên kết (link)"};
    private static final String[] TYPE_VALUES = {StudyMaterial.TYPE_TEXT, StudyMaterial.TYPE_LINK};
    private static final String[] CATEGORIES = {
            "Part 1", "Part 2", "Part 3", "Part 4", "Part 5", "Part 6", "Part 7",
            "Ngữ pháp", "Từ vựng"
    };

    private FragmentMaterialManagementBinding binding;
    private MaterialViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMaterialManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MaterialViewModel.class);

        MaterialAdapter adapter = new MaterialAdapter(new MaterialAdapter.Listener() {
            @Override
            public void onItemClick(StudyMaterial item) {
                showEditDialog(item);
            }

            @Override
            public void onItemLongClick(StudyMaterial item) {
                confirmDelete(item);
            }
        });
        binding.rvMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMaterials.setAdapter(adapter);

        binding.btnAdd.setOnClickListener(v -> showEditDialog(null));

        // Màn quản lý luôn hiện tất cả danh mục
        viewModel.setCategory("");
        viewModel.getMaterials().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    // material = null: thêm mới, khác null: sửa
    private void showEditDialog(@Nullable StudyMaterial material) {
        DialogMaterialEditBinding d = DialogMaterialEditBinding.inflate(getLayoutInflater());

        d.spType.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, TYPE_LABELS));
        d.spCategory.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, CATEGORIES));

        if (material != null) {
            d.etTitle.setText(material.getTitle());
            d.etDescription.setText(material.getDescription());
            d.etLevel.setText(material.getLevel());
            d.etContent.setText(material.getContent());
            d.spType.setSelection(StudyMaterial.TYPE_LINK.equals(material.getType()) ? 1 : 0);
            for (int i = 0; i < CATEGORIES.length; i++) {
                if (CATEGORIES[i].equals(material.getCategory())) {
                    d.spCategory.setSelection(i);
                    break;
                }
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(material == null ? "Thêm tài liệu" : "Sửa tài liệu")
                .setView(d.getRoot())
                .setPositiveButton("Lưu", null)   // gán sau để kiểm tra dữ liệu mà không đóng hộp thoại
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(di ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String title = d.etTitle.getText().toString().trim();
                    String content = d.etContent.getText().toString().trim();
                    if (title.isEmpty()) {
                        d.etTitle.setError("Vui lòng nhập tiêu đề");
                        return;
                    }
                    if (content.isEmpty()) {
                        d.etContent.setError("Vui lòng nhập nội dung hoặc link");
                        return;
                    }

                    StudyMaterial m = material != null ? material : new StudyMaterial();
                    m.setTitle(title);
                    m.setDescription(d.etDescription.getText().toString().trim());
                    m.setType(TYPE_VALUES[d.spType.getSelectedItemPosition()]);
                    m.setCategory(CATEGORIES[d.spCategory.getSelectedItemPosition()]);
                    m.setLevel(d.etLevel.getText().toString().trim());
                    m.setContent(content);
                    viewModel.save(m);
                    Toast.makeText(requireContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }));
        dialog.show();
    }

    private void confirmDelete(StudyMaterial item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa tài liệu")
                .setMessage("Xóa \"" + item.getTitle() + "\"?")
                .setPositiveButton("Xóa", (dlg, which) -> viewModel.delete(item.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}