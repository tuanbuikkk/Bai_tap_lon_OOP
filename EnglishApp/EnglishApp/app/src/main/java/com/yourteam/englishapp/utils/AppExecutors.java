package com.yourteam.englishapp.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Luồng nền dùng chung cho mọi thao tác DB (Android cấm mạng trên main thread). */
public final class AppExecutors {
    private static final ExecutorService IO = Executors.newFixedThreadPool(4);

    private AppExecutors() {}

    public static ExecutorService io() {
        return IO;
    }
}
