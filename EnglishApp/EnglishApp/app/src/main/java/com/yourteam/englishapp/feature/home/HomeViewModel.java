package com.yourteam.englishapp.feature.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.model.AccessLevel;
import com.yourteam.englishapp.service.SubscriptionService;
import com.yourteam.englishapp.session.UserSession;
import com.yourteam.englishapp.utils.AppExecutors;
import com.yourteam.englishapp.utils.Result;

/** Thông tin user + gói hiện tại cho màn Home. */
public class HomeViewModel extends AndroidViewModel {
    private final UserSession session;
    private final SubscriptionService subscriptionService = new SubscriptionService();

    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<AccessLevel> level = new MutableLiveData<>(AccessLevel.FREE);
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public HomeViewModel(@NonNull Application app) {
        super(app);
        session = UserSession.getInstance(app);
    }

    public LiveData<User> getUser() { return user; }
    public LiveData<AccessLevel> getAccessLevel() { return level; }
    public LiveData<String> getMessage() { return message; }
    public LiveData<Boolean> getLoading() { return loading; }
    public void clearMessage() { message.setValue(null); }

    /** Nạp lại user + gói (gọi ở onResume để gói hết hạn được cập nhật). */
    public void load() {
        User u = session.getCurrentUser();
        user.setValue(u);
        if (u == null) return;
        AppExecutors.io().execute(() -> {
            Result<AccessLevel> r = subscriptionService.getCurrentAccessLevel(u.getId());
            if (r.isSuccess()) level.postValue(r.getData());
            else message.postValue(r.getMessage());
        });
    }

    /** Dùng giá trị đã nạp để bật/tắt nút; kiểm tra thật nằm ở SubscriptionService.canAccess. */
    public boolean canAccess(AccessLevel required) {
        AccessLevel cur = level.getValue();
        return cur != null && cur.canAccess(required);
    }

    /** Mô phỏng nâng cấp Premium 30 ngày (chưa có cổng thanh toán). */
    public void upgradePremium() {
        User u = session.getCurrentUser();
        if (u == null) return;
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            Result<?> r = subscriptionService.activate(u.getId(), "PREMIUM_MONTH", 30, AccessLevel.PREMIUM);
            if (r.isSuccess()) {
                level.postValue(AccessLevel.PREMIUM);
                message.postValue("Đã kích hoạt Premium 30 ngày");
            } else {
                message.postValue(r.getMessage());
            }
            loading.postValue(false);
        });
    }

    public void logout() {
        session.logout();
    }
}
