package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpRequestForgetPassword {
    /**
     * Send Post method url ("/m/forgetPassword").
     * It send mail to the user with with token to restore his password.
     * @param email Is the email from the form.
     */
    public void forgetPasswordPost(String email){

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.forgetPassword(email);

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
