package com.hbv2.icelandevents.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbv2.icelandevents.HttpRequest.HttpRequestForgetPassword;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseForgotPassword;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener{
    private ConnectivityManager cm;

    @Required(order = 1)
    @Email(order = 2, message= "Please enter a valid email address.")
    private EditText emailText;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
        String email = emailText.getText().toString();
        new HttpRequestForgetPassword().forgetPasswordPost(email);
    }


    @Override
    public void onValidationSucceeded() {
        if(NetworkChecker.isOnline(cm)){
            sendMail();
        }else{
            Toast.makeText(this, "Network isn't avilable",Toast.LENGTH_LONG).show();
        }
    }

    public void onValidationFailed(View view, Rule<?> rule) {
        final String failureMessage = rule.getFailureMessage();
        if (view instanceof EditText) {
            EditText failed = (EditText) view;
            failed.requestFocus();
            failed.setError(failureMessage);
        } else {
            Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onHttp(HttpResponseForgotPassword response) {
        if(response.getCode() == 200){
            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        }
        Log.d("Gögn frá index", "tóskt: " +response.getCode());
    }
}
