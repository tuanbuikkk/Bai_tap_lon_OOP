package com.yourteam.englishapp.utils;

/** Kết quả của một thao tác nghiệp vụ: thành công + dữ liệu, hoặc thất bại + thông báo. */
public final class Result<T> {
    private final boolean success;
    private final T data;
    private final String message;

    private Result(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(false, null, message);
    }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}
