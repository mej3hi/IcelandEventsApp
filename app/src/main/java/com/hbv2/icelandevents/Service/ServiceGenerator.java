package com.hbv2.icelandevents.Service;


import android.text.TextUtils;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import okhttp3.Credentials;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 26.1.2017.
 */

public class ServiceGenerator {

    private static final String BASE_URL = "http://192.168.1.103:8080";
    //private static final String BASE_URL = "http://localhost:8080";


    /*private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();*/

    /*private static Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();*/

    /*public static <S> S createService(Class<S> serviceClass){

        return retrofit.create(serviceClass);
    }*/

/// =====================================================================================





    private static  OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass){
       return createService(serviceClass, null ,null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password){
        if (!TextUtils.isEmpty(username) && !(TextUtils.isEmpty(password))) {
            String authToken = Credentials.basic(username,password);
            return createService(serviceClass,authToken);
        }
        return createService(serviceClass,null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken){
        if(!TextUtils.isEmpty(authToken)){

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization",authToken);
                    requestBuilder.header("Accept","application/json");
                    requestBuilder.method(original.method(),original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);

                }
            });

        }

        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

}
