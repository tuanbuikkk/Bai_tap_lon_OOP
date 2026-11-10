package com.example.englishapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.englishapp.feature.dictionary.DictionaryFragment;
import com.example.englishapp.feature.material.MaterialListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.englishapp.feature.admin.MaterialManagementFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.getMenu().findItem(R.id.nav_admin).setVisible(AdminConfig.isAdmin());
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dictionary) {
                switchTo(new DictionaryFragment());
                return true;
            } else if (id == R.id.nav_materials) {
                switchTo(new MaterialListFragment());
                return true;
            } else if (id == R.id.nav_admin) {
                switchTo(new MaterialManagementFragment());
                return true;
            }
            return false;
        });
        nav.setOnItemReselectedListener(item -> { /* bấm lại tab đang chọn: không làm gì */ });

        if (savedInstanceState == null) {
            switchTo(new DictionaryFragment());
        }
    }

    // Đổi tab: xóa các màn con (ví dụ màn kết quả từ điển) rồi thay fragment
    private void switchTo(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}