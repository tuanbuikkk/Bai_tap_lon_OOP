package com.example.englishapp.repository;

import androidx.lifecycle.LiveData;

import com.example.englishapp.data.local.entity.StudyMaterial;

import java.util.List;
public interface StudyMaterialRepository {
    LiveData<List<StudyMaterial>> getAll();

    LiveData<List<StudyMaterial>> getByCategory(String category);

    LiveData<StudyMaterial> getById(long id);

    void insert(StudyMaterial material);

    void update(StudyMaterial material);

    void deleteById(long id);
}
