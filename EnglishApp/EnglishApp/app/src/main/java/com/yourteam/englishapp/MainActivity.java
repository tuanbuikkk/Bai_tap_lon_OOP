package com.yourteam.englishapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.databinding.ActivityMainBinding;
import com.yourteam.englishapp.model.Role;
import com.yourteam.englishapp.service.AuthService;
import com.yourteam.englishapp.session.UserSession;
import com.yourteam.englishapp.utils.AppExecutors;
import com.yourteam.englishapp.utils.Result;

/**
 * 1 Activity + nhiều Fragment. Khi khởi động (luồng nền): tạo admin mặc định, nạp lại phiên,
 * rồi chọn startDestination (Home / AdminHome / Login). Có route guard cho màn admin.
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = UserSession.getInstance(this);
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = host.getNavController();
        navController.addOnDestinationChangedListener(this::guardAdminRoutes);

        bootstrap();
    }

    private void bootstrap() {
        binding.progressStartup.setVisibility(View.VISIBLE);
        AppExecutors.io().execute(() -> {
            AuthService auth = new AuthService(session);
            String warning = null;

            Result<User> admin = auth.ensureDefaultAdmin();
            if (!admin.isSuccess()) warning = admin.getMessage();

            Result<User> restored = auth.restoreSession();
            int start = R.id.loginFragment;
            if (restored.isSuccess() && restored.getData() != null) {
                start = restored.getData().getRole() == Role.ADMIN
                        ? R.id.adminHomeFragment : R.id.homeFragment;
            } else if (!restored.isSuccess()) {
                warning = restored.getMessage();   // mất kết nối DB / tài khoản bị khóa -> về Login
            }

            final int startDestination = start;
            final String warn = warning;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                NavGraph graph = navController.getNavInflater().inflate(R.navigation.nav_graph);
                graph.setStartDestination(startDestination);
                navController.setGraph(graph);
                binding.progressStartup.setVisibility(View.GONE);
                if (warn != null) Toast.makeText(this, warn, Toast.LENGTH_LONG).show();
            });
        });
    }

    /** Route guard: chỉ ADMIN được vào màn admin; sai thì đẩy về Home (hoặc Login nếu chưa đăng nhập). */
    private void guardAdminRoutes(NavController controller, NavDestination dest, Bundle args) {
        int id = dest.getId();
        boolean adminScreen = id == R.id.adminHomeFragment || id == R.id.userManagementFragment;
        if (adminScreen && !session.isAdmin()) {
            binding.getRoot().post(() -> controller.navigate(
                    session.isLoggedIn() ? R.id.action_global_home : R.id.action_global_login));
        }
    }
}
