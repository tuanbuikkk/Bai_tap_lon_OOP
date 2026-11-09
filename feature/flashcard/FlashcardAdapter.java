package com.example.toeicapp.feature.flashcard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapp.data.local.entity.Flashcard;
import com.example.toeicapp.databinding.ItemFlashcardBinding;

public class FlashcardAdapter extends ListAdapter<Flashcard, FlashcardAdapter.FlashcardHolder> {

    public interface OnFlashcardListener {
        void onClick(Flashcard flashcard);

        void onDelete(Flashcard flashcard);
    }

    private final OnFlashcardListener listener;

    public FlashcardAdapter(OnFlashcardListener listener) {
        super(new DiffUtil.ItemCallback<Flashcard>() {
            @Override
            public boolean areItemsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
                return oldItem.name.equals(newItem.name)
                        && oldItem.description.equals(newItem.description);
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlashcardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFlashcardBinding binding = ItemFlashcardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FlashcardHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardHolder holder, int position) {
        Flashcard flashcard = getItem(position);
        holder.binding.tvName.setText(flashcard.name);
        holder.binding.tvDescription.setText(flashcard.description);
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(flashcard));
        holder.binding.btnDelete.setOnClickListener(v -> listener.onDelete(flashcard));
    }

    static class FlashcardHolder extends RecyclerView.ViewHolder {
        final ItemFlashcardBinding binding;

        FlashcardHolder(ItemFlashcardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
