package com.yourteam.englishapp.feature.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.yourteam.englishapp.R;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.databinding.FragmentLoginBinding;
import com.yourteam.englishapp.model.Role;
import com.yourteam.englishapp.utils.ValidationUtils;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvGoRegister.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_login_to_register));

        vm.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean busy = Boolean.TRUE.equals(loading);
            binding.progress.setVisibility(busy ? View.VISIBLE : View.GONE);
            binding.btnLogin.setEnabled(!busy);
        });

        vm.getLoginResult().observe(getViewLifecycleOwner(), r -> {
            if (r == null) return;
            vm.clearLoginResult();
            if (r.isSuccess()) {
                goByRole(r.getData());
            } else {
                binding.tvError.setText(r.getMessage());
                binding.tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void attemptLogin() {
        binding.tvError.setVisibility(View.GONE);
        String email = text(binding.etEmail.getText());
        String pass = text(binding.etPassword.getText());

        boolean ok = true;
        if (!ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.setError(getString(R.string.err_email));
            ok = false;
        } else {
            binding.tilEmail.setError(null);
        }
        if (pass.isEmpty()) {
            binding.tilPassword.setError(getString(R.string.err_password_empty));
            ok = false;
        } else {
            binding.tilPassword.setError(null);
        }
        if (ok) vm.login(email, pass);
    }

    private void goByRole(User u) {
        int action = u.getRole() == Role.ADMIN
                ? R.id.action_global_adminHome
                : R.id.action_global_home;
        NavHostFragment.findNavController(this).navigate(action);
    }

    private static String text(CharSequence cs) {
        return cs == null ? "" : cs.toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
