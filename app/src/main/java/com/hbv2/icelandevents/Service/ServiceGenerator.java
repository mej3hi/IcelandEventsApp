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

public class ServiceGenerator {

    /**
     * Is the Domain Name System (DNS) of the homepage.
     */
    private static final String BASE_URL = "https://hugbunadarverkefni2.herokuapp.com/";

    /**
     * Here we crate httpClient to add header to the Http request.
     */
    private static  OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    /**
     * Here we crate builder so we can call Http request and listen to Http respond.
     */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit;

    /**
     * Here we crate standard server.
     * @return createService(serviceClass, null ,null)
     */
    public static <S> S createService(Class<S> serviceClass){
       return createService(serviceClass, null ,null);
    }

    /**
     * Here we create server with Credentials from username and password
     * if the username and password is not empty.
     * @return It return createService(serviceClass,authToken) or  createService(serviceClass,null);
     */
    public static <S> S createService(Class<S> serviceClass, String username, String password){
        if (!TextUtils.isEmpty(username) && !(TextUtils.isEmpty(password))) {
            String authToken = Credentials.basic(username,password);
            return createService(serviceClass,authToken);
        }
        return createService(serviceClass,null);
    }

    /**
     * Here we create server with authToken and add it to the
     * header "Authorization" and authToken to it and
     * also "Accept","application/json". to it
     * @param authToken AuthToken is the Credentials from username and password.
     * @return retrofit.create(serviceClass);
     */
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
