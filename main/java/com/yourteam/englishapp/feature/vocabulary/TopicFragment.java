package com.yourteam.englishapp.feature.vocabulary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yourteam.englishapp.data.local.entity.Topic;
import com.yourteam.englishapp.databinding.FragmentTopicBinding;
import com.yourteam.englishapp.databinding.ItemTopicBinding;

public class TopicFragment extends Fragment {
    private FragmentTopicBinding binding;
    private VocabularyViewModel vm;
    private TopicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTopicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(VocabularyViewModel.class);

        adapter = new TopicAdapter(topic -> {
            Bundle bundle = new Bundle();
            bundle.putInt("topic_id", topic.getId());
            bundle.putString("topic_name", topic.getName());
            NavHostFragment.findNavController(this).navigate(
                    com.yourteam.englishapp.R.id.vocabularyFragment, bundle);
        });

        binding.recyclerTopics.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerTopics.setAdapter(adapter);

        vm.getTopics().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            binding.tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading ->
                binding.progress.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE));

        vm.loadTopics();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class TopicAdapter extends ListAdapter<Topic, TopicAdapter.VH> {
        public interface OnTopicClickListener {
            void onTopicClick(Topic topic);
        }

        private final OnTopicClickListener listener;

        public TopicAdapter(OnTopicClickListener listener) {
            super(new DiffUtil.ItemCallback<Topic>() {
                @Override
                public boolean areItemsTheSame(@NonNull Topic oldItem, @NonNull Topic newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Topic oldItem, @NonNull Topic newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            });
            this.listener = listener;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(ItemTopicBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Topic item = getItem(position);
            holder.binding.tvTopicName.setText(item.getName());
            holder.binding.tvTopicDesc.setText(item.getDescription());
            holder.binding.tvWordCount.setText(item.getTotalWords() + " từ");
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTopicClick(item);
            });
        }

        static class VH extends RecyclerView.ViewHolder {
            final ItemTopicBinding binding;

            VH(ItemTopicBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}