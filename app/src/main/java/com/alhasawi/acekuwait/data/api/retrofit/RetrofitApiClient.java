package com.alhasawi.acekuwait.data.api.retrofit;

import android.provider.Settings;

import com.alhasawi.acekuwait.ui.base.AceHardware;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    //    public static final String BASE_URL = "https://preprod.acekuwait.com/api/v0/";
    public static final String BASE_URL = "https://www.acekuwait.com/api/v0/";
    private static RetrofitApiClient instance = null;
    private ApiInterface apiInterface;

    private RetrofitApiClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .addHeader("User-Agent", "Ace Hardware")
                        .addHeader("DEVICE-OS", "ANDROID")
                        .addHeader("DEVICE-ID", Settings.Secure.getString(AceHardware.appContext.getContentResolver(), Settings.Secure.ANDROID_ID))
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitApiClient getInstance() {
        if (instance == null) {
            instance = new RetrofitApiClient();
        }
        return instance;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

}
