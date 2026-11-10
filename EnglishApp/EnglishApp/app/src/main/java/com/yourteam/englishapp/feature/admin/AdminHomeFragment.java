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
import androidx.navigation.fragment.NavHostFragment;
import com.yourteam.englishapp.R;
import com.yourteam.englishapp.databinding.FragmentAdminHomeBinding;
import com.yourteam.englishapp.session.UserSession;

/** Dashboard admin: thống kê nhanh + lối vào quản lý user. */
public class AdminHomeFragment extends Fragment {
    private FragmentAdminHomeBinding binding;
    private AdminViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AdminViewModel.class);
        UserSession session = UserSession.getInstance(requireContext());

        vm.getTotalUsers().observe(getViewLifecycleOwner(), n ->
                binding.tvTotalUsers.setText(String.valueOf(n)));
        vm.getPremiumUsers().observe(getViewLifecycleOwner(), n ->
                binding.tvPremiumUsers.setText(String.valueOf(n)));
        vm.getMessage().observe(getViewLifecycleOwner(), m -> {
            if (m == null) return;
            vm.clearMessage();
            Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show();
        });

        binding.btnManageUsers.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_adminHome_to_userManagement));
        binding.btnLogout.setOnClickListener(v -> {
            session.logout();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_login);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.loadStats();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
