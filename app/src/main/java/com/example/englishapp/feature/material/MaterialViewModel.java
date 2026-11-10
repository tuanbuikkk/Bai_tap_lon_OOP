package com.example.englishapp.feature.material;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.englishapp.data.local.AppDatabase;
import com.example.englishapp.data.local.entity.StudyMaterial;
import com.example.englishapp.repository.StudyMaterialRepositoryImpl;
import com.example.englishapp.service.StudyMaterialService;

import java.util.List;

public class MaterialViewModel extends AndroidViewModel {
    private final StudyMaterialService service;
    private final MutableLiveData<String> category = new MutableLiveData<>("");
    private final LiveData<List<StudyMaterial>> materials;

    public MaterialViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        service = new StudyMaterialService(
                new StudyMaterialRepositoryImpl(db.studyMaterialDao()));

        // Đổi danh mục thì danh sách tự đổi theo
        materials = Transformations.switchMap(category, c -> service.getMaterials(c));
    }

    public LiveData<List<StudyMaterial>> getMaterials() {
        return materials;
    }

    public String getCategory() {
        String c = category.getValue();
        return c == null ? "" : c;
    }

    // "" = tất cả danh mục
    public void setCategory(String c) {
        category.setValue(c == null ? "" : c);
    }

    public LiveData<StudyMaterial> getMaterial(long id) {
        return service.getMaterial(id);
    }

    public void save(StudyMaterial material) {
        service.save(material);
    }

    public void delete(long id) {
        service.delete(id);
    }

    @Override
    protected void onCleared() {
        service.shutdown();
    }
}