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

public interface UserAPI  {

    @GET("/m/signIn")
    Call<String> getSignIn();

    @POST("/m/signUp")
    Call<String> postSignUp(@Body User userForm);

    @POST("/m/forgetPassword")
    Call<String> forgetPassword(@Query("email") String email);

    @POST("/m/resetPassword")
    Call<String> resetPassword(@Query("token") String token, @Query("password") String password,
                             @Query("passwordConfirm") String passwordConf);
}
