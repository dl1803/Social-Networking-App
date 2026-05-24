package com.example.emptyproject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://blackntt.net:8111/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        // thêm cấu hình retry khi kết nối thất bại cho OkHttpClient(obj gửi nhận data từ S)
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    // gắn url api để liên kết đến server
                    .baseUrl(BASE_URL)
                    // Bộ dịch Gson : auto dịch JSON từ server <-> Obj Java
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }



}
