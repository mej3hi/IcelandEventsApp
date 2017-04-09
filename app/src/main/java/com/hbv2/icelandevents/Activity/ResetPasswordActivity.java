package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestUser;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


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
    private ProgressBar loadingDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);
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
     * Click listener for Change Password Button when clicked
     * calls validator to validate the Change password form.
     * @param view view is the GUI component
     */
    public void changePasswordOnClick(View view) {
        validator.validate();
    }

    /**
     * Validation for the Reset password form was successful,
     * therefore the changePassword() method is called.
     */
    @Override
    public void onValidationSucceeded() {
        changePassword();
    }

    /**
     * Validation did not succeed, thereby the first error message is shown
     * @param view View is the GUI component
     * @param rule Rule contains the error message
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
     * Sends HttpRequest containing a password and token
     * Requesting to change the password.
     * Also checks for internet connection before sending it.
     */
    public void changePassword(){
        if(NetworkChecker.isOnline(this)){
            loadingDisplay.setVisibility(View.VISIBLE);
            String token = confirmCode.getText().toString();
            String password = newPassword.getText().toString();
            String passwordConf = confirmPassword.getText().toString();
            new HttpRequestUser().resetPasswordPost(token,password,passwordConf);
        }
        else{
            PopUpMsg.toastMsg("Network isn´t available",this);
        }
    }

    /**
     * Receiving the Respond from the backend server.
     * @param response Response has the Code and Message from backend server
     */
    @Subscribe
    public void onResetPassword(HttpResponseMsg response) {
        loadingDisplay.setVisibility(View.INVISIBLE);
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
     * Redirects user to SignInActivity.
     */
    public void redirectToSignIn(){
        Intent intent = new Intent(this,SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
