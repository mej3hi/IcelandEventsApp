package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestCall;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.Service.ServiceGenerator;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;

public class ResetPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Required(order = 1)
    private EditText confirmCode;

    @Required(order = 2)
    @Password(order = 3)
    @TextRule(order = 4, minLength = 8,maxLength = 32,message = "Please use between 6 and 32 characters.")
    private EditText newPassword;

    @Required(order = 5)
    @ConfirmPassword(order = 6)
    @TextRule(order = 7, minLength = 8,maxLength = 32, message = "Please use between 6 and 32 characters.")
    private EditText confirmPassword;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        confirmCode = (EditText) findViewById(R.id.confirmCodeText);
        newPassword = (EditText) findViewById(R.id.newPasswText);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswText);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Her we listen to the Change Password Button for on click and
     * call on validation to check the Rest password from.
     * @param view view is the GUI components
     */
    public void changePasswOnClick(View view) {
        validator.validate();
    }

    /**
     * Here we send the Rest password from to the backend server and also check for
     * internet connection before sending it.
     */
    public void changePasswordConfirm(){
        if(NetworkChecker.isOnline(this)){
            String token = confirmCode.getText().toString();
            String password = newPassword.getText().toString();
            String passwordConf = confirmPassword.getText().toString();
            Call<String> call = ServiceGenerator.createService(UserAPI.class).resetPassword(token,password,passwordConf);
            HttpRequestCall.callReponseMsg(call);
        }
        else{
            PopUpMsg.toastMsg("Network isn´t available",this);
        }
    }

    /**
     * If the validation of the Rest password from is succeeded
     * then we call in changePasswordConfirm method.
     */
    @Override
    public void onValidationSucceeded() {
        changePasswordConfirm();
    }

    /**
     *  If the validation find error on the Rest password from, then we show error msg.
     * @param view View is the GUI components
     * @param rule Rule is the error msg
     */
    @Override
    public void onValidationFailed(View view, Rule<?> rule) {
        final String failureMessage = rule.getFailureMessage();
        if (view instanceof EditText) {
            EditText failed = (EditText) view;
            failed.requestFocus();
            failed.setError(failureMessage);
        } else {
            PopUpMsg.toastMsg(failureMessage,this);
        }
    }

    /**
     * Here we get the Respond from the backend server.
     * @param response Response has the Code and Msg from backend server.
     */
    @Subscribe
    public void onResetPassword(HttpResponseMsg response) {
        if(response.getCode() == 200 && response.getMsg().equals("ok")){
            redirectToSignIn();
        }else if(response.getCode() == 200 && response.getMsg().equals("token_expires")){
            EditText failed = confirmCode;
            failed.requestFocus();
            failed.setError("The token has already been expires. Request a new one");
        }else{
            String title ="Something went wrong";
            String msg = "Something went wrong with the Reset password, please try again";
            PopUpMsg.dialogMsg(title,msg,this);
        }
    }


    /**
     * Here we go back to the Sign in from,
     * we call on Intent for SignInActivity.class
     */
    public void redirectToSignIn(){
        Intent intent = new Intent(ResetPasswordActivity.this,SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
