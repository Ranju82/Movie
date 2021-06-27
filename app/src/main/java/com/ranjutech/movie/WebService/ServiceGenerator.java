package com.ranjutech.movie.WebService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ranjutech.movie.Utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit retrofit=null;
    private static Gson gson=new GsonBuilder().create();

    private static HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder okHttpClientBuilder=new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    HttpUrl url=chain.request().url().newBuilder().addQueryParameter("api_key",Constants.API_KEY).build();
                    Request request=chain.request().newBuilder()
                            .url(url)
                            .build();
                    return chain.proceed(request);
                }
            });
    private static OkHttpClient okHttpClient=okHttpClientBuilder.build();


    public static <T> T createService(Class<T> serviceClass){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return  retrofit.create(serviceClass);
    }
}
