package com.yourteam.englishapp.feature.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.yourteam.englishapp.R;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.databinding.ItemUserBinding;
import com.yourteam.englishapp.model.Role;

public class UserAdapter extends ListAdapter<User, UserAdapter.VH> {

    public interface Listener {
        void onToggleActive(User u);
        void onToggleRole(User u);
        void onDelete(User u);
    }

    private final Listener listener;
    private final long currentUserId;

    public UserAdapter(long currentUserId, Listener listener) {
        super(new DiffUtil.ItemCallback<User>() {
            @Override public boolean areItemsTheSame(@NonNull User a, @NonNull User b) {
                return a.getId() == b.getId();
            }
            @Override public boolean areContentsTheSame(@NonNull User a, @NonNull User b) {
                return a.isActive() == b.isActive() && a.getRole() == b.getRole()
                        && a.getFullName().equals(b.getFullName())
                        && a.getEmail().equals(b.getEmail());
            }
        });
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        User u = getItem(position);
        ItemUserBinding b = h.binding;
        boolean self = u.getId() == currentUserId;

        b.tvName.setText(self ? u.getFullName() + " (bạn)" : u.getFullName());
        b.tvEmail.setText(u.getEmail());
        b.tvInfo.setText(u.getRole().name() + " • " + (u.isActive() ? "Hoạt động" : "Đã khóa"));

        b.btnLock.setText(u.isActive() ? R.string.action_lock : R.string.action_unlock);
        b.btnRole.setText(u.getRole() == Role.ADMIN ? R.string.action_make_user : R.string.action_make_admin);

        // Admin không được tự khóa / đổi quyền / xóa chính mình
        b.btnLock.setEnabled(!self);
        b.btnRole.setEnabled(!self);
        b.btnDelete.setEnabled(!self);

        b.btnLock.setOnClickListener(v -> listener.onToggleActive(u));
        b.btnRole.setOnClickListener(v -> listener.onToggleRole(u));
        b.btnDelete.setOnClickListener(v -> listener.onDelete(u));
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemUserBinding binding;
        VH(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
