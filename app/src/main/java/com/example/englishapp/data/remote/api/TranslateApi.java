package com.example.englishapp.data.remote.api;

import com.example.englishapp.data.remote.dto.TranslateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateApi {
    // langPair: "en|vi". email: truyền null để bỏ qua; truyền email thật để tăng hạn mức/ngày
    @GET("get")
    Call<TranslateResponse> translate(
            @Query("q") String text,
            @Query("langpair") String langPair,
            @Query("de") String email);
}
