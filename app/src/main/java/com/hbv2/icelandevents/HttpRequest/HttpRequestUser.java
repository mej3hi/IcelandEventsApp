package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import retrofit2.Call;



public class HttpRequestUser {

    /**
     * Sends Get method url ("/m/signIn")
     * for signing the user in.
     * @param username Is the username from the form
     * @param password Is the password form the form
     */
    public void signInGet(String username, String password){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class, username, password);
        Call<String> call = userAPI.getSignIn();
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Sends Post method url ("/m/signUp")
     * for signing up new user.
     * @param user the new user that is to be signed up
     */
    public void signUpPost(User user){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.postSignUp(user);
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Sends Post method url ("/m/forgetPassword")
     * for sending mail to the user with a token to restore his password.
     * @param email Is the email from the form.
     */
    public void forgetPasswordPost(String email) {
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.forgetPassword(email);
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Sends Post method url ("/resetPassword")
     * for storing the new password to database.
     * @param token Is the token for that user
     * @param password Is the password that the user created
     */
    public void resetPasswordPost(String token,String password,String passwordConf){
        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class);
        Call<String> call = userAPI.resetPassword(token, password,passwordConf);
        HttpRequestCall.callReponseMsg(call);
    }

}
