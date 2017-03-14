package com.hbv2.icelandevents.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestForgetPassword;
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

    public void resetPasswBtnOnClick(View view) {
        validator.validate();
    }

    public void sendMail(){
        if(NetworkChecker.isOnline(this)){
            String email = emailText.getText().toString();
            new HttpRequestForgetPassword().forgetPasswordPost(email);
        }else{
            PopUpMsg.toastMsg("Network isn't avilable",this);
        }
    }


    @Override
    public void onValidationSucceeded() {
            sendMail();
    }

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

    @Subscribe
    public void onForgotPassword(HttpResponseMsg response) {
        if(response.getCode() == 200 && response.getMsg().equals("ok")){
            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        }
        else if(response.getCode() == 200 && response.getMsg().equals("invalid_email")){
            PopUpMsg.toastMsg("Invalid email",this);
        }
        else{
            String title ="Something went wrong";
            String msg = "Something went wrong with the Forgot password, please try again";
            PopUpMsg.dialogMsg(title,msg,this);
        }
    }

}
