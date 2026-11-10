package com.example.englishapp.repository;

import androidx.lifecycle.LiveData;

import com.example.englishapp.data.local.dao.StudyMaterialDao;
import com.example.englishapp.data.local.entity.StudyMaterial;

import java.util.List;

public class StudyMaterialRepositoryImpl implements StudyMaterialRepository {
    private final StudyMaterialDao dao;

    public StudyMaterialRepositoryImpl(StudyMaterialDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<StudyMaterial>> getAll() {
        return dao.getAll();
    }

    @Override
    public LiveData<List<StudyMaterial>> getByCategory(String category) {
        return dao.getByCategory(category);
    }

    @Override
    public LiveData<StudyMaterial> getById(long id) {
        return dao.getById(id);
    }

    // insert, update, delete phải chạy trên luồng nền (Service lo việc này)
    @Override
    public void insert(StudyMaterial material) {
        dao.insert(material);
    }

    @Override
    public void update(StudyMaterial material) {
        dao.update(material);
    }

    @Override
    public void deleteById(long id) {
        dao.deleteById(id);
    }
}