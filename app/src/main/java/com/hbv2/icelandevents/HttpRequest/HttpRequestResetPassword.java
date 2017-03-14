package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 22.2.2017.
 */

public class HttpRequestResetPassword {

    /**
     * Send Post method url ("/resetPassword".
     * It store the new password that user has create.
     * @param token Is the token for that user.
     * @param password Is the password that user create.
     */
    public void resetPasswordPost(String token,String password,String passwordConf){

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.resetPassword(token,password,passwordConf);

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
