package com.example.englishapp.feature.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.databinding.ItemHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
    public interface Listener {
        void onItemClick(DictionaryHistory item);

        void onDeleteClick(DictionaryHistory item);
    }

    private final List<DictionaryHistory> items = new ArrayList<>();
    private final Listener listener;

    public DictionaryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<DictionaryHistory> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged(); // danh sách tối đa 20 mục nên cách đơn giản này là đủ
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding b = ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DictionaryHistory item = items.get(position);
        holder.binding.tvWord.setText(item.getWord());

        // Dòng phụ: ưu tiên nghĩa tiếng Việt, không có thì hiện phiên âm
        String sub = item.getMeaningVi();
        if (sub == null || sub.isEmpty()) sub = item.getPhonetic();
        boolean hasSub = sub != null && !sub.isEmpty();
        holder.binding.tvMeaning.setVisibility(hasSub ? View.VISIBLE : View.GONE);
        holder.binding.tvMeaning.setText(hasSub ? sub : "");

        holder.binding.getRoot().setOnClickListener(v -> listener.onItemClick(item));
        holder.binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemHistoryBinding binding;

        ViewHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
