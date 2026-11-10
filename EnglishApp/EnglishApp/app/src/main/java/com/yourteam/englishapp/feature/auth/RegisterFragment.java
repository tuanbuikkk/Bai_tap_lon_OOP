package com.yourteam.englishapp.feature.auth;

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
import com.yourteam.englishapp.databinding.FragmentRegisterBinding;
import com.yourteam.englishapp.utils.ValidationUtils;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AuthViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.tvGoLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        vm.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean busy = Boolean.TRUE.equals(loading);
            binding.progress.setVisibility(busy ? View.VISIBLE : View.GONE);
            binding.btnRegister.setEnabled(!busy);
        });

        vm.getRegisterResult().observe(getViewLifecycleOwner(), r -> {
            if (r == null) return;
            vm.clearRegisterResult();
            if (r.isSuccess()) {
                Toast.makeText(requireContext(), R.string.register_success, Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                binding.tvError.setText(r.getMessage());
                binding.tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void attemptRegister() {
        binding.tvError.setVisibility(View.GONE);
        String name = text(binding.etName.getText());
        String email = text(binding.etEmail.getText());
        String pass = binding.etPassword.getText() == null ? "" : binding.etPassword.getText().toString();
        String confirm = binding.etConfirm.getText() == null ? "" : binding.etConfirm.getText().toString();

        boolean ok = true;
        ok &= setError(binding.tilName, !ValidationUtils.isValidName(name), R.string.err_name);
        ok &= setError(binding.tilEmail, !ValidationUtils.isValidEmail(email), R.string.err_email);
        ok &= setError(binding.tilPassword, !ValidationUtils.isValidPassword(pass), R.string.err_password_rule);
        ok &= setError(binding.tilConfirm, !pass.equals(confirm), R.string.err_confirm);
        if (ok) vm.register(name, email, pass, confirm);
    }

    private boolean setError(com.google.android.material.textfield.TextInputLayout til,
                             boolean invalid, int msgRes) {
        til.setError(invalid ? getString(msgRes) : null);
        return !invalid;
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
