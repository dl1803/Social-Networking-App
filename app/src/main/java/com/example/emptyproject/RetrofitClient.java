package com.example.emptyproject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://blackntt.net:8111/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    // gắn url api để liên kết đến server
                    .baseUrl(BASE_URL)
                    // Bộ dịch Gson : auto dịch JSON từ server <-> Obj Java
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}
