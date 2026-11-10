package com.example.englishapp.feature.dictionary;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishapp.data.remote.dto.DictionaryResponse;
import com.example.englishapp.databinding.FragmentDictionaryResultBinding;
import com.example.englishapp.databinding.ItemMeaningBinding;
import com.example.englishapp.model.WordResult;

import java.util.List;
import java.util.Locale;

public class DictionaryResultFragment extends Fragment {
    private static final String TAG = "AUDIO_DEBUG";
    private static final int MAX_DEFINITIONS = 3;
    private static final int MAX_SYNONYMS = 5;

    private FragmentDictionaryResultBinding binding;
    private MediaPlayer player;
    private TextToSpeech tts;
    private boolean ttsReady = false;
    private String currentWord = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDictionaryResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());

        DictionaryViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(DictionaryViewModel.class);
        WordResult result = viewModel.getSelectedResult();
        if (result == null) {
            getParentFragmentManager().popBackStack();
            return;
        }

        tts = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int r = tts.setLanguage(Locale.US);
                ttsReady = r != TextToSpeech.LANG_MISSING_DATA
                        && r != TextToSpeech.LANG_NOT_SUPPORTED;
            }
            Log.d(TAG, "TTS init status=" + status + ", ready=" + ttsReady);
        });

        bind(result);
    }

    private void bind(WordResult r) {
        currentWord = r.getWord();
        binding.tvWord.setText(r.getWord());
        setTextOrHide(binding.tvPhonetic, r.getPhonetic());
        setTextOrHide(binding.tvMeaningVi, r.getMeaningVi());

        // Nút Nghe luôn hiện: ưu tiên audio của API, lỗi thì đọc bằng TTS
        final String audio = r.getAudioUrl();
        binding.btnAudio.setVisibility(View.VISIBLE);
        binding.btnAudio.setOnClickListener(v -> {
            if (audio != null && !audio.isEmpty()) playAudio(audio);
            else speakWithTts();
        });

        LayoutInflater inflater = getLayoutInflater();
        for (DictionaryResponse.Meaning m : r.getMeanings()) {
            ItemMeaningBinding mb = ItemMeaningBinding.inflate(inflater, binding.llMeanings, false);
            mb.tvPartOfSpeech.setText(m.getPartOfSpeech());

            StringBuilder sb = new StringBuilder();
            var defs = m.getDefinitions();
            if (defs != null) {
                int n = 0;
                for (var d : defs) {
                    if (++n > MAX_DEFINITIONS) break;
                    if (sb.length() > 0) sb.append("\n\n");
                    sb.append(n).append(". ").append(d.getDefinition());
                    String example = d.getExample();
                    if (example != null && !example.isEmpty()) {
                        sb.append("\n    Ví dụ: ").append(example);
                    }
                }
            }
            mb.tvDefinitions.setText(sb.toString());

            List<String> syn = m.getSynonyms();
            if (syn != null && !syn.isEmpty()) {
                List<String> shown = syn.subList(0, Math.min(MAX_SYNONYMS, syn.size()));
                mb.tvSynonyms.setText("Đồng nghĩa: " + TextUtils.join(", ", shown));
            } else {
                mb.tvSynonyms.setVisibility(View.GONE);
            }

            binding.llMeanings.addView(mb.getRoot());
        }
    }

    private void setTextOrHide(TextView tv, String text) {
        boolean has = text != null && !text.isEmpty();
        tv.setVisibility(has ? View.VISIBLE : View.GONE);
        tv.setText(has ? text : "");
    }

    private void playAudio(String url) {
        releasePlayer();
        if (url.startsWith("//")) url = "https:" + url;
        Log.d(TAG, "url = " + url);
        try {
            player = new MediaPlayer();
            player.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());
            player.setDataSource(url);
            player.setOnPreparedListener(mp -> {
                int d = mp.getDuration();
                Log.d(TAG, "duration = " + d + " ms");
                if (d > 0 && d < 300) {      // file rỗng/im lặng -> đọc bằng TTS
                    releasePlayer();
                    speakWithTts();
                } else {
                    mp.start();
                }
            });
            player.setOnCompletionListener(mp -> releasePlayer());
            player.setOnErrorListener((mp, what, extra) -> {
                Log.d(TAG, "MediaPlayer error what=" + what + " extra=" + extra);
                releasePlayer();
                speakWithTts();
                return true;
            });
            player.prepareAsync();
        } catch (Exception e) {
            Log.d(TAG, "playAudio exception: " + e);
            releasePlayer();
            speakWithTts();
        }
    }

    private void speakWithTts() {
        if (!isAdded()) return;
        if (tts != null && ttsReady) {
            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, "word");
        } else {
            Toast.makeText(requireContext(), "Không phát được âm thanh.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        binding = null;
    }
}