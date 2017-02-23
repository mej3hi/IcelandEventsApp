package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 22.2.2017.
 */

public class HttpRequestForgetPassword {
    /**
     * Send Post method url ("/m/forgetPassword").
     * It send mail to the user with with token to restore his password.
     * @param email Is the email from the form.
     */
    public void forgetPasswordPost(String email){

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<Void> call = userAPI.getSignIn();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
               // EventBus.getDefault().post(new HttpResponseSignIn(response.code()));

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Failuress :" +t);

            }
        });
    }
}
