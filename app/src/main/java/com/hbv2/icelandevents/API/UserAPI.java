package com.hbv2.icelandevents.API;


import com.hbv2.icelandevents.Entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Martin on 6.2.2017.
 */

public interface UserAPI {

    @GET("/m/signIn")
    Call<Void> getSignIn();

    @POST("/m/signUp")
    Call<Void> postSignUp(@Body User user);

    @GET("/m/forgetPassword")
    Call<Void> forgetPassword(@Query("email") String email);

    @GET("/m/resetPassword")
    Call<Void> resetPassword(@Query("token") String token, @Query("password") String password);
}
