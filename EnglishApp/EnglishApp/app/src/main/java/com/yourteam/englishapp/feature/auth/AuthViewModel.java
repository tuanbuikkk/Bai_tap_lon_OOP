package com.yourteam.englishapp.feature.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yourteam.englishapp.data.local.entity.User;
import com.yourteam.englishapp.service.AuthService;
import com.yourteam.englishapp.session.UserSession;
import com.yourteam.englishapp.utils.AppExecutors;
import com.yourteam.englishapp.utils.Result;

/** Giữ state màn Login/Register; đẩy việc DB sang luồng nền, trả kết quả qua postValue. */
public class AuthViewModel extends AndroidViewModel {
    private final AuthService authService;
    private final MutableLiveData<Result<User>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Result<User>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public AuthViewModel(@NonNull Application app) {
        super(app);
        authService = new AuthService(UserSession.getInstance(app));
    }

    public LiveData<Result<User>> getLoginResult() { return loginResult; }
    public LiveData<Result<User>> getRegisterResult() { return registerResult; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void login(String email, String pass) {
        if (Boolean.TRUE.equals(loading.getValue())) return;
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            Result<User> r = authService.login(email, pass);
            loading.postValue(false);
            loginResult.postValue(r);
        });
    }

    public void register(String name, String email, String pass, String confirm) {
        if (Boolean.TRUE.equals(loading.getValue())) return;
        loading.setValue(true);
        AppExecutors.io().execute(() -> {
            Result<User> r = authService.register(name, email, pass, confirm);
            loading.postValue(false);
            registerResult.postValue(r);
        });
    }

    /** Gọi sau khi UI đã xử lý kết quả để không bị phát lại khi xoay màn hình. */
    public void clearLoginResult() { loginResult.setValue(null); }
    public void clearRegisterResult() { registerResult.setValue(null); }
}
