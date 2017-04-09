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
    private static final String BASE_URL = "http://10.0.2.2:8080";

    /**
     * Here we created HttpClient to add header to the HttpRequest.
     */
    private static  OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    /**
     * Here we created builder so we can send HttpRequest and listen to HttpRespond.
     */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit;

    /**
     * Here we created standard server.
     * @return createService(serviceClass, null ,null)
     */
    public static <S> S createService(Class<S> serviceClass){
       return createService(serviceClass, null ,null);
    }

    /**
     * Here we created server with Credentials from username and password
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
     * Here we created server with authToken and added it to the
     * header "Authorization" and also added
     * "Accept","application/json" to the header.
     * @param authToken AuthToken is the Credentials from username and password
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
