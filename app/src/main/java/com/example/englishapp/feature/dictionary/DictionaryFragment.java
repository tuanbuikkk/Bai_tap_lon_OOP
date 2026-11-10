package com.example.englishapp.feature.dictionary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.databinding.FragmentDictionaryBinding;
import com.example.englishapp.model.WordResult;
import com.example.englishapp.R;

public class DictionaryFragment extends Fragment {
    private FragmentDictionaryBinding binding;
    private DictionaryViewModel viewModel;
    private DictionaryAdapter adapter;
    private String lastQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gắn ViewModel với Activity để màn kết quả (bước sau) dùng chung được
        viewModel = new ViewModelProvider(requireActivity()).get(DictionaryViewModel.class);

        adapter = new DictionaryAdapter(new DictionaryAdapter.Listener() {
            @Override
            public void onItemClick(DictionaryHistory item) {
                binding.etWord.setText(item.getWord());
                search(item.getWord());
            }

            @Override
            public void onDeleteClick(DictionaryHistory item) {
                viewModel.deleteHistory(item.getId());
            }
        });
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistory.setAdapter(adapter);

        binding.btnSearch.setOnClickListener(v -> search(binding.etWord.getText().toString()));
        binding.etWord.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(binding.etWord.getText().toString());
                return true;
            }
            return false;
        });
        binding.btnRetry.setOnClickListener(v -> search(lastQuery));
        binding.btnClearHistory.setOnClickListener(v -> viewModel.clearHistory());

        viewModel.getRecentHistory().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmptyHistory.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.btnClearHistory.setVisibility(empty ? View.GONE : View.VISIBLE);
        });

        viewModel.getUiState().observe(getViewLifecycleOwner(), this::render);
    }

    private void search(String word) {
        lastQuery = word;
        hideKeyboard();
        viewModel.search(word);
    }

    private void render(DictionaryUiState state) {
        boolean loading = state.getStatus() == DictionaryUiState.Status.LOADING;
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSearch.setEnabled(!loading);

        switch (state.getStatus()) {
            case NOT_FOUND:
                showMessage(state.getMessage(), false);
                break;
            case ERROR:
                showMessage(state.getMessage(), true);
                break;
            case SUCCESS:
                hideMessage();
                viewModel.openResult(state.getData());
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DictionaryResultFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            default: // IDLE, LOADING
                hideMessage();
        }
    }

    private void showMessage(String message, boolean showRetry) {
        binding.llMessage.setVisibility(View.VISIBLE);
        binding.tvMessage.setText(message);
        binding.btnRetry.setVisibility(showRetry ? View.VISIBLE : View.GONE);
    }

    private void hideMessage() {
        binding.llMessage.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.etWord.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
