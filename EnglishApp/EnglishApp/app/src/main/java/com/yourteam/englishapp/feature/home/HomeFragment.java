package com.yourteam.englishapp.feature.home;

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
import com.yourteam.englishapp.R;
import com.yourteam.englishapp.databinding.FragmentHomeBinding;
import com.yourteam.englishapp.model.AccessLevel;

/**
 * Màn hình chính. Các nút chức năng của nhóm:
 * khi một thành viên xong module, thay 0 bằng id destination của họ trong nav_graph
 * (ví dụ R.id.vocabFragment) - xem openFeature(...) bên dưới.
 */
public class HomeFragment extends Fragment {
    private static final int NOT_INTEGRATED = 0;

    private FragmentHomeBinding binding;
    private HomeViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(HomeViewModel.class);

        vm.getUser().observe(getViewLifecycleOwner(), u -> {
            if (u != null) binding.tvGreeting.setText(getString(R.string.home_greeting, u.getFullName()));
        });
        vm.getAccessLevel().observe(getViewLifecycleOwner(), l -> {
            boolean premium = l == AccessLevel.PREMIUM;
            binding.tvPlan.setText(premium ? R.string.plan_premium : R.string.plan_free);
            binding.btnUpgrade.setVisibility(premium ? View.GONE : View.VISIBLE);
            binding.btnPractice.setAlpha(vm.canAccess(AccessLevel.PREMIUM) ? 1f : 0.5f);
        });
        vm.getLoading().observe(getViewLifecycleOwner(), b ->
                binding.btnUpgrade.setEnabled(!Boolean.TRUE.equals(b)));
        vm.getMessage().observe(getViewLifecycleOwner(), m -> {
            if (m == null) return;
            vm.clearMessage();
            Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show();
        });

        // Id destination của các module khác: thay NOT_INTEGRATED khi đã có trong nav_graph
        binding.btnVocab.setOnClickListener(v -> openFeature("Từ vựng", NOT_INTEGRATED, AccessLevel.FREE));
        binding.btnLessons.setOnClickListener(v -> openFeature("Bài học", NOT_INTEGRATED, AccessLevel.FREE));
        binding.btnProgress.setOnClickListener(v -> openFeature("Tiến độ", NOT_INTEGRATED, AccessLevel.FREE));
        binding.btnPractice.setOnClickListener(v -> openFeature("Bài tập / kiểm tra", NOT_INTEGRATED, AccessLevel.PREMIUM));

        binding.btnUpgrade.setOnClickListener(v -> vm.upgradePremium());
        binding.btnLogout.setOnClickListener(v -> {
            vm.logout();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_login);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.load();
    }

    private void openFeature(String name, int destinationId, AccessLevel required) {
        if (!vm.canAccess(required)) {
            Toast.makeText(requireContext(), R.string.need_premium, Toast.LENGTH_SHORT).show();
            return;
        }
        if (destinationId == NOT_INTEGRATED) {
            Toast.makeText(requireContext(), getString(R.string.module_not_ready, name), Toast.LENGTH_SHORT).show();
            return;
        }
        NavHostFragment.findNavController(this).navigate(destinationId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
