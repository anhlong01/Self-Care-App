package com.lph.selfcareapp.serviceAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RetrofitInstance {
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;
    private static Retrofit apiInterface = null;
//    private static String BASE_URL ="http://10.0.2.2:8080/";
    private static String BASE_URL ="https://edoc.kma.io.vn/";
    private static String IMAGE_URL="https://anh.moe/";
    public static Retrofit createRetrofit(String authToken){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Thêm interceptor để xử lý header Authorization
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Nếu không có token hoặc request không cần token, gửi request bình thường
            if (authToken == null || original.header("No-Auth") != null) {
                return chain.proceed(original);
            }

            // Nếu có token, thêm header Authorization
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + authToken)
                    .method(original.method(), original.body());

            return chain.proceed(requestBuilder.build());
        });

        // Thêm logging interceptor (chỉ cho debug)
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
            .build();

    }

    public static ApiService getPagingService(String authToken){
        if(apiInterface==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                // Nếu có token, thêm header Authorization
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + authToken)
                        .method(original.method(), original.body());

                return chain.proceed(requestBuilder.build());
            });

            apiInterface = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return apiInterface.create(ApiService.class);
    }


    public static ApiService getService(String authToken){
        return createRetrofit(authToken).create(ApiService.class);
    }

    public static ApiService getServiceWithoutAuth(){
        return createRetrofit(null).create(ApiService.class);
    }

    public static ApiService getImage(){
        if(retrofit2==null)
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(IMAGE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

        return retrofit2.create(ApiService.class);
    }

}
