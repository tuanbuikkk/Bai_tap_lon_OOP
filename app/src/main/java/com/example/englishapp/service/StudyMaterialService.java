package com.example.englishapp.service;

import androidx.lifecycle.LiveData;

import com.example.englishapp.data.local.entity.StudyMaterial;
import com.example.englishapp.repository.StudyMaterialRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyMaterialService {
    private final StudyMaterialRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public StudyMaterialService(StudyMaterialRepository repository) {
        this.repository = repository;
    }

    // category rỗng hoặc null nghĩa là lấy tất cả
    public LiveData<List<StudyMaterial>> getMaterials(String category) {
        if (category == null || category.isEmpty()) return repository.getAll();
        return repository.getByCategory(category);
    }

    public LiveData<StudyMaterial> getMaterial(long id) {
        return repository.getById(id);
    }

    // id = 0 là tài liệu mới (thêm), id khác 0 là sửa
    public void save(StudyMaterial material) {
        executor.execute(() -> {
            if (material.getId() == 0) {
                material.setCreatedAt(System.currentTimeMillis());
                repository.insert(material);
            } else {
                repository.update(material);
            }
        });
    }

    public void delete(long id) {
        executor.execute(() -> repository.deleteById(id));
    }

    public void shutdown() {
        executor.shutdown();
    }
}