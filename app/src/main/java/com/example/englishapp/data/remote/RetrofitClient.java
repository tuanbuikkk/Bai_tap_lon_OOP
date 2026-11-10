package com.example.englishapp.data.remote;

import com.example.englishapp.data.remote.api.DictionaryApi;
import com.example.englishapp.data.remote.api.TranslateApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String DICTIONARY_BASE_URL = "https://api.dictionaryapi.dev/api/v2/";
    private static final String TRANSLATE_BASE_URL = "https://api.mymemory.translated.net/";

    private static OkHttpClient okHttpClient;
    private static DictionaryApi dictionaryApi;
    private static TranslateApi translateApi;

    private RetrofitClient() {
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // chỉ để debug, sau này đổi sang NONE

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build();
        }
        return okHttpClient;
    }

    private static Retrofit buildRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized DictionaryApi getDictionaryApi() {
        if (dictionaryApi == null) {
            dictionaryApi = buildRetrofit(DICTIONARY_BASE_URL).create(DictionaryApi.class);
        }
        return dictionaryApi;
    }

    public static synchronized TranslateApi getTranslateApi() {
        if (translateApi == null) {
            translateApi = buildRetrofit(TRANSLATE_BASE_URL).create(TranslateApi.class);
        }
        return translateApi;
    }
}
