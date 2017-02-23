package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 22.2.2017.
 */

public class HttpResetPassword {

    /**
     * Send Post method url ("/resetPassword".
     * It store the new password that user has create.
     * @param token Is the token for that user.
     * @param password Is the password that user create.
     * @param passwordConfirm Is the same as password.
     */
    public void resetPasswordPost(String token,String password,String passwordConfirm){

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<Void> call = userAPI.login();

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
