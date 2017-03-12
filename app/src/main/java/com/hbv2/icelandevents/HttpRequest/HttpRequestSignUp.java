package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 22.2.2017.
 */

public class HttpRequestSignUp {

    /**
     * Send Post method url ("/m/signUp").
     * @param user Get the registration value from the form.
     */
    public void signUpPost(User user){

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.postSignUp(user);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseMsg(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failuress :" +t);
                EventBus.getDefault().post(new HttpResponseMsg("",500));
            }
        });
    }
}
