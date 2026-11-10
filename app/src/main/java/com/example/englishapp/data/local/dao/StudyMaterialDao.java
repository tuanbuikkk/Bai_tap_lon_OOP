package com.example.englishapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishapp.data.local.entity.StudyMaterial;

import java.util.List;

@Dao
public interface StudyMaterialDao {
    @Insert
    long insert(StudyMaterial material);

    @Insert
    void insertAll(List<StudyMaterial> materials);

    @Update
    void update(StudyMaterial material);

    @Query("DELETE FROM study_material WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * FROM study_material ORDER BY createdAt DESC")
    LiveData<List<StudyMaterial>> getAll();

    @Query("SELECT * FROM study_material WHERE category = :category ORDER BY createdAt DESC")
    LiveData<List<StudyMaterial>> getByCategory(String category);

    @Query("SELECT * FROM study_material WHERE id = :id")
    LiveData<StudyMaterial> getById(long id);

    @Query("SELECT COUNT(*) FROM study_material")
    int count();
}
