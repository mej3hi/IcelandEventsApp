package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 21.2.2017.
 */

public class HttpRequestSignIn {
    /**
     * Send Get method url ("/m/signIn").
     * It Sign In the user
     * @param username Is the username from the form
     * @param password Is the password form the form
     */
   public void signInGet(String username, String password){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class, username, password);
        Call<String> call = userAPI.getSignIn();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseMsg(response.body(), response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failuress :" +t);
                EventBus.getDefault().post(new HttpResponseMsg("", 500));
            }
        });
    }


    /**
     * Send Get method url ("/m/signIn").
     * It auto Sign In the user
     * @param username Is the username from rhe UserInfo.
     * @param password Is the password form the UserInfo.
     */
    public void autoSignInGet(String username, String password){

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class,username,password);
        Call<String> call = userAPI.getSignIn();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseMsg(response.body(), response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failuress :" +t);
                EventBus.getDefault().post(new HttpResponseMsg("", 500));

            }
        });
    }


}
