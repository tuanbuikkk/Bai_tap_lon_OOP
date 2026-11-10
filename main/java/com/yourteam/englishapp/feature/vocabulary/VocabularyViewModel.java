package com.yourteam.englishapp.feature.vocabulary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yourteam.englishapp.data.local.entity.Topic;
import com.yourteam.englishapp.data.local.entity.Vocabulary;
import com.yourteam.englishapp.repository.TopicRepository;
import com.yourteam.englishapp.repository.TopicRepositoryImpl;
import com.yourteam.englishapp.repository.VocabularyRepository;
import com.yourteam.englishapp.repository.VocabularyRepositoryImpl;
import com.yourteam.englishapp.service.VocabularyService;
import com.yourteam.englishapp.utils.AppExecutors;

import java.sql.SQLException;
import java.util.List;

public class VocabularyViewModel extends AndroidViewModel {
    private static final String MSG_DB = "Không kết nối được cơ sở dữ liệu";

    private final TopicRepository topicRepo = new TopicRepositoryImpl();
    private final VocabularyRepository vocabRepo = new VocabularyRepositoryImpl();
    private final VocabularyService vocabService = new VocabularyService(vocabRepo);

    private final MutableLiveData<List<Topic>> topics = new MutableLiveData<>();
    private final MutableLiveData<List<Vocabulary>> vocabularies = new MutableLiveData<>();
    private final MutableLiveData<Vocabulary> selectedVocabulary = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public VocabularyViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Topic>> getTopics() { return topics; }
    public LiveData<List<Vocabulary>> getVocabularies() { return vocabularies; }
    public LiveData<Vocabulary> getSelectedVocabulary() { return selectedVocabulary; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getMessage() { return message; }
    public void clearMessage() { message.setValue(null); }

    public void loadTopics() {
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            try {
                List<Topic> list = topicRepo.getAllTopics();
                topics.postValue(list);
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            } finally {
                loading.postValue(false);
            }
        });
    }

    public void loadVocabulariesByTopic(int topicId) {
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            try {
                List<Vocabulary> list = vocabService.getVocabulariesByTopic(topicId);
                vocabularies.postValue(list);
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            } finally {
                loading.postValue(false);
            }
        });
    }

    public void loadVocabularyDetail(int id) {
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            try {
                Vocabulary v = vocabService.getVocabularyDetail(id);
                selectedVocabulary.postValue(v);
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            } finally {
                loading.postValue(false);
            }
        });
    }

    public void saveVocabulary(Vocabulary v) {
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            try {
                vocabService.saveVocabulary(v);
                message.postValue("Lưu từ vựng thành công");
                loadVocabulariesByTopic(v.getTopicId());
            } catch (IllegalArgumentException e) {
                message.postValue(e.getMessage());
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            } finally {
                loading.postValue(false);
            }
        });
    }

    public void deleteVocabulary(int id, int topicId) {
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            try {
                vocabService.deleteVocabulary(id);
                message.postValue("Đã xóa từ vựng");
                loadVocabulariesByTopic(topicId);
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            } finally {
                loading.postValue(false);
            }
        });
    }
}