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
                // Interceptor là trung gian giữa client và server, có chức năng sửa request, thêm header trc khi gửi lên server
                .addInterceptor(chain -> {
                    // chain là chuỗi request gửi lên server, dùng để lấy request hiện tại
                    okhttp3.Request original = chain.request();
                    // Thêm Header "Connection: close" : yêu cầu đóng socket k cần giữ lại connection(do cơ chế Keep-Alive Connection tự giữ lại kết nối để  chạy nhanh hơn)
                    // giúp kh tái sử dụng connection cũ khi bị lỗi (vd lỗi hiện tại : "unexpected end of stream" do bị đóng socket bất ngờ khi đang đọc response)
                    // gây chậm hơn -> chỉ dùng khi bị lỗi
                    okhttp3.Request request = original.newBuilder()
                            .header("Connection", "close")
                            .build();
                    return chain.proceed(request); // gửi request đã update đến server
                })
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
