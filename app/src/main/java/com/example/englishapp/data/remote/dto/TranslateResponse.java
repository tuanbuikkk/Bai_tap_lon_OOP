package com.example.englishapp.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class TranslateResponse {
    @SerializedName("responseData")
    private ResponseData responseData;

    // Để String cho an toàn: kiểu của trường này có thể là số hoặc chuỗi tùy trường hợp
    @SerializedName("responseStatus")
    private String responseStatus;

    @SerializedName("responseDetails")
    private String responseDetails;

    public String getResponseStatus() {
        return responseStatus;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public boolean isSuccess() {
        return "200".equals(responseStatus);
    }

    public String getTranslatedText() {
        if (responseData == null || responseData.translatedText == null) {
            return "";
        }
        return responseData.translatedText;
    }

    public static class ResponseData {
        @SerializedName("translatedText")
        private String translatedText;
    }
}
