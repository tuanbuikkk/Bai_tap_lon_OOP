package com.example.englishapp.feature.material;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishapp.data.local.entity.StudyMaterial;
import com.example.englishapp.databinding.FragmentMaterialDetailBinding;

public class MaterialDetailFragment extends Fragment {
    private static final String ARG_ID = "material_id";

    private FragmentMaterialDetailBinding binding;

    public static MaterialDetailFragment newInstance(long id) {
        MaterialDetailFragment f = new MaterialDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMaterialDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());

        long id = requireArguments().getLong(ARG_ID);
        MaterialViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(MaterialViewModel.class);

        viewModel.getMaterial(id).observe(getViewLifecycleOwner(), m -> {
            if (m == null) {   // tài liệu đã bị xóa (ví dụ từ màn Admin)
                getParentFragmentManager().popBackStack();
                return;
            }
            bind(m);
        });
    }

    private void bind(StudyMaterial m) {
        binding.tvTitle.setText(m.getTitle());
        binding.tvMeta.setText(m.getCategory() + "  •  " + m.getLevel());
        binding.tvDescription.setText(m.getDescription());

        if (StudyMaterial.TYPE_LINK.equals(m.getType())) {
            binding.tvContent.setText(m.getContent());   // hiện luôn đường link
            binding.btnOpenLink.setVisibility(View.VISIBLE);
            binding.btnOpenLink.setOnClickListener(v -> openLink(m.getContent()));
        } else {
            binding.tvContent.setText(m.getContent());
            binding.btnOpenLink.setVisibility(View.GONE);
        }
    }

    private void openLink(String url) {
        if (url == null || url.trim().isEmpty()) return;
        String u = url.trim();
        if (!u.startsWith("http://") && !u.startsWith("https://")) u = "https://" + u;
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(u)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Không mở được liên kết.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}