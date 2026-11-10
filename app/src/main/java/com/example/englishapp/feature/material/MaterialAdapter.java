package com.example.englishapp.feature.material;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishapp.data.local.entity.StudyMaterial;
import com.example.englishapp.databinding.ItemMaterialBinding;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    public interface Listener {
        void onItemClick(StudyMaterial item);

        default void onItemLongClick(StudyMaterial item) {
        }
    }

    private final List<StudyMaterial> items = new ArrayList<>();
    private final Listener listener;

    public MaterialAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<StudyMaterial> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMaterialBinding b = ItemMaterialBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudyMaterial m = items.get(position);
        holder.binding.tvTitle.setText(m.getTitle());
        holder.binding.tvDescription.setText(m.getDescription());

        String typeLabel = StudyMaterial.TYPE_LINK.equals(m.getType()) ? "Liên kết" : "Bài đọc";
        holder.binding.tvMeta.setText(m.getCategory() + "  •  " + m.getLevel()
                + "  •  " + typeLabel);

        holder.binding.getRoot().setOnClickListener(v -> listener.onItemClick(m));
        holder.binding.getRoot().setOnLongClickListener(v -> {
            listener.onItemLongClick(m);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMaterialBinding binding;

        ViewHolder(ItemMaterialBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}