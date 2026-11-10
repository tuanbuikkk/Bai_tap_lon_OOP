package com.yourteam.englishapp.feature.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.yourteam.englishapp.R;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.databinding.FragmentUserManagementBinding;

/** Danh sách user + tìm kiếm + khóa/mở, đổi quyền, xóa. */
public class UserManagementFragment extends Fragment implements UserAdapter.Listener {
    private FragmentUserManagementBinding binding;
    private AdminViewModel vm;
    private UserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new UserAdapter(vm.currentUserId(), this);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { vm.search(q); return true; }
            @Override public boolean onQueryTextChange(String q) { vm.search(q); return true; }
        });

        vm.getUsers().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            binding.tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
        });
        vm.getLoading().observe(getViewLifecycleOwner(), b ->
                binding.progress.setVisibility(Boolean.TRUE.equals(b) ? View.VISIBLE : View.GONE));
        vm.getMessage().observe(getViewLifecycleOwner(), m -> {
            if (m == null) return;
            vm.clearMessage();
            Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show();
        });

        vm.search("");
    }

    @Override public void onToggleActive(User u) { vm.toggleActive(u); }

    @Override public void onToggleRole(User u) { vm.toggleRole(u); }

    @Override
    public void onDelete(User u) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_delete_title)
                .setMessage(getString(R.string.confirm_delete_msg, u.getEmail()))
                .setPositiveButton(R.string.action_delete, (d, w) -> vm.delete(u))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
