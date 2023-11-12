package com.example.assignment;

import com.google.gson.annotations.SerializedName;

public class ApodResponse {
    @SerializedName("title")
    private String title;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("date")
    private String date;

    @SerializedName("url")
    private String url;

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
