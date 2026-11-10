package com.example.englishapp.data.remote.api;

import com.example.englishapp.data.remote.dto.DictionaryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface DictionaryApi {
    // Từ không tồn tại -> API trả HTTP 404 (không phải mảng rỗng)
    @GET("entries/en/{word}")
    Call<List<DictionaryResponse>> getDefinition(@Path("word") String word);
}
