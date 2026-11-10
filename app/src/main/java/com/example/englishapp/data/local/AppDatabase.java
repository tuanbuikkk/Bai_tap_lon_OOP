package com.example.englishapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.englishapp.data.local.dao.DictionaryHistoryDao;
import com.example.englishapp.data.local.dao.StudyMaterialDao;
import com.example.englishapp.data.local.entity.DictionaryHistory;
import com.example.englishapp.data.local.entity.StudyMaterial;

@Database(entities = {DictionaryHistory.class, StudyMaterial.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DictionaryHistoryDao dictionaryHistoryDao();

    public abstract StudyMaterialDao studyMaterialDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "english_app.db")
                            .fallbackToDestructiveMigration() // tạm: đổi cấu trúc bảng thì xóa DB cũ
                            .build();
                    seedIfEmpty(INSTANCE);
                }
            }
        }
        return INSTANCE;
    }

    // Bảng tài liệu trống thì chèn dữ liệu mẫu (chạy nền, LiveData tự cập nhật khi có dữ liệu)
    private static void seedIfEmpty(AppDatabase db) {
        new Thread(() -> {
            StudyMaterialDao dao = db.studyMaterialDao();
            if (dao.count() == 0) {
                dao.insertAll(MaterialSeedData.create());
            }
        }).start();
    }
}
