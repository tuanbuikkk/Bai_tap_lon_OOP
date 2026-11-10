package com.yourteam.englishapp.feature.admin;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.Role;
import com.yourteam.englishapp.repository.UserRepository;
import com.yourteam.englishapp.repository.UserRepositoryImpl;
import com.yourteam.englishapp.service.SubscriptionService;
import com.yourteam.englishapp.session.UserSession;
import com.yourteam.englishapp.utils.AppExecutors;
import com.yourteam.englishapp.utils.Result;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Dùng chung cho AdminHomeFragment (thống kê) và UserManagementFragment (danh sách + thao tác). */
public class AdminViewModel extends AndroidViewModel {
    private static final String MSG_DB = "Không kết nối được cơ sở dữ liệu";

    private final UserRepository userRepo = new UserRepositoryImpl();
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final UserSession session;

    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalUsers = new MutableLiveData<>();
    private final MutableLiveData<Integer> premiumUsers = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final AtomicInteger searchSeq = new AtomicInteger();
    private String lastKeyword = "";

    public AdminViewModel(@NonNull Application app) {
        super(app);
        session = UserSession.getInstance(app);
    }

    public LiveData<List<User>> getUsers() { return users; }
    public LiveData<Integer> getTotalUsers() { return totalUsers; }
    public LiveData<Integer> getPremiumUsers() { return premiumUsers; }
    public LiveData<String> getMessage() { return message; }
    public LiveData<Boolean> getLoading() { return loading; }
    public void clearMessage() { message.setValue(null); }
    public long currentUserId() { return session.getUserId(); }

    public void loadStats() {
        AppExecutors.io().execute(() -> {
            try {
                totalUsers.postValue(userRepo.count());
            } catch (SQLException e) {
                message.postValue(MSG_DB);
                return;
            }
            Result<Integer> p = subscriptionService.countPremiumUsers();
            if (p.isSuccess()) premiumUsers.postValue(p.getData());
        });
    }

    public void search(String keyword) {
        lastKeyword = keyword == null ? "" : keyword;
        final int seq = searchSeq.incrementAndGet();   // bỏ kết quả của lần tìm cũ về trễ
        loading.postValue(true);   // postValue: search() cũng được gọi từ luồng nền (reload)
        AppExecutors.io().execute(() -> {
            try {
                List<User> list = userRepo.search(lastKeyword);
                if (seq == searchSeq.get()) users.postValue(list);
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            }
            if (seq == searchSeq.get()) loading.postValue(false);
        });
    }

    public void reload() { search(lastKeyword); }

    /** Khóa / mở khóa. Admin không được khóa chính mình. */
    public void toggleActive(User u) {
        if (isSelf(u)) return;
        runAction(() -> userRepo.setActive(u.getId(), !u.isActive()),
                u.isActive() ? "Đã khóa tài khoản" : "Đã mở khóa tài khoản");
    }

    /** Đổi quyền USER <-> ADMIN. Admin không được đổi quyền chính mình. */
    public void toggleRole(User u) {
        if (isSelf(u)) return;
        Role next = u.getRole() == Role.ADMIN ? Role.USER : Role.ADMIN;
        runAction(() -> userRepo.setRole(u.getId(), next), "Đã đổi quyền thành " + next.name());
    }

    /** Xóa user. Admin không được tự xóa chính mình. */
    public void delete(User u) {
        if (isSelf(u)) return;
        runAction(() -> userRepo.delete(u.getId()), "Đã xóa người dùng");
    }

    private boolean isSelf(User u) {
        if (!session.isAdmin()) {                       // chặn gọi từ phiên không phải admin
            message.setValue("Bạn không có quyền thực hiện thao tác này");
            return true;
        }
        if (u.getId() == session.getUserId()) {
            message.setValue("Không thể thao tác trên chính tài khoản của bạn");
            return true;
        }
        return false;
    }

    private interface Action { void run() throws SQLException; }

    private void runAction(Action action, String okMessage) {
        AppExecutors.io().execute(() -> {
            try {
                action.run();
                message.postValue(okMessage);
                reload();   // post lên main thread bên dưới
            } catch (SQLException e) {
                message.postValue(MSG_DB);
            }
        });
    }
}
