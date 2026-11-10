package com.yourteam.englishapp.feature.vocabulary;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yourteam.englishapp.data.local.entity.Vocabulary;
import com.yourteam.englishapp.databinding.ItemVocabularyBinding;

public class VocabularyAdapter extends ListAdapter<Vocabulary, VocabularyAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(Vocabulary vocabulary);
    }

    private final OnItemClickListener listener;

    public VocabularyAdapter(OnItemClickListener listener) {
        super(new DiffUtil.ItemCallback<Vocabulary>() {
            @Override
            public boolean areItemsTheSame(@NonNull Vocabulary oldItem, @NonNull Vocabulary newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Vocabulary oldItem, @NonNull Vocabulary newItem) {
                return oldItem.getWord().equals(newItem.getWord())
                        && oldItem.getMeaning().equals(newItem.getMeaning());
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemVocabularyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Vocabulary item = getItem(position);
        holder.binding.tvWord.setText(item.getWord());
        holder.binding.tvMeaning.setText(item.getMeaning());
        holder.binding.tvPhonetic.setText(item.getPhonetic());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemVocabularyBinding binding;

        VH(ItemVocabularyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}