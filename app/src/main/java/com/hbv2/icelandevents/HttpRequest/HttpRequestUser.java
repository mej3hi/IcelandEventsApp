package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import retrofit2.Call;



public class HttpRequestUser {


    /**
     * Send Get method url ("/m/signIn").
     * It Sign In the user
     * @param username Is the username from the form
     * @param password Is the password form the form
     */
    public void signInGet(String username, String password){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class, username, password);
        Call<String> call = userAPI.getSignIn();
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Send Post method url ("/m/signUp").
     * @param user Get the registration value from the form.
     */
    public void signUpPost(User user){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.postSignUp(user);
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Send Post method url ("/m/forgetPassword").
     * It send mail to the user with with token to restore his password.
     * @param email Is the email from the form.
     */
    public void forgetPasswordPost(String email) {
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.forgetPassword(email);
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Send Post method url ("/resetPassword".
     * It store the new password that user has create.
     * @param token Is the token for that user.
     * @param password Is the password that user create.
     */
    public void resetPasswordPost(String token,String password,String passwordConf){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.resetPassword(token, password,passwordConf);
        HttpRequestCall.callReponseMsg(call);
    }

}
