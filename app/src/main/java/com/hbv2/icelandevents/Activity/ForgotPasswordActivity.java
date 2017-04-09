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
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener{

    private ProgressBar loadingDisplay;

    @Required(order = 1)
    @Email(order = 2, message= "Please enter a valid email address.")
    private EditText emailText;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);
        emailText = (EditText) findViewById(R.id.emailText);
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
     * Sends HttpRequest containing email address
     * requesting an email response with reset password token.
     * Also checks for internet connection before sending it.
     */
    public void sendMail(){
        if(NetworkChecker.isOnline(this)){
            loadingDisplay.setVisibility(View.VISIBLE);
            String email = emailText.getText().toString();
            new HttpRequestUser().forgetPasswordPost(email);
        }else{
            PopUpMsg.toastMsg("Network isn't avilable",this);
        }
    }

    /**
     * Click listener for Reset password button when clicked
     * calls validator to validate the Email form.
     * @param view view is the GUI component
     */
    public void resetPasswBtnOnClick(View view) {
        validator.validate();
    }

    /**
     * Validation for the Email form was successful therefore sendMail() method is called.
     */
    @Override
    public void onValidationSucceeded() {
            sendMail();
    }

    /**
     * Validation did not succeed, thereby the first error message is shown.
     * @param view View is the GUI component
     * @param rule Rule contains the error message
     */
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
     * Receiving Respond from the backend server.
     * @param response Response has the Code and the Message from backend server
     */
    @Subscribe
    public void onForgotPassword(HttpResponseMsg response) {
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(response.getCode() == 200 && response.getMsg().equals("ok")){
            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        }
        else if(response.getCode() == 200 && response.getMsg().equals("invalid_email")){
            EditText failed = emailText;
            failed.requestFocus();
            failed.setError("Invalid email");
        }
        else{
            String title ="Something went wrong";
            String msg = "Something went wrong with the Forgot password, please try again";
            PopUpMsg.dialogMsg(title,msg,this);
        }
    }

}
