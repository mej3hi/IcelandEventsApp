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

import com.hbv2.icelandevents.HttpRequest.HttpRequestResetPassword;
import com.hbv2.icelandevents.HttpResponse.HttpResponseForgotPassword;
import com.hbv2.icelandevents.HttpResponse.HttpResponseResetPassword;
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
    private ConnectivityManager cm;

    @Required(order = 1)
    private EditText confirmCode;

    @Required(order = 2)
    @Password(order = 3)
    @TextRule(order = 4, minLength = 8, message = "Please use at least 8 characters")
    private EditText newPassword;

    @Required(order = 5)
    @ConfirmPassword(order = 6)
    @TextRule(order = 7, minLength = 8, message = "Please use at least 8 characters")
    private EditText confirmPassword;

    Validator validator;


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

        Intent intent = getIntent();

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


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

    public void changePasswOnClick(View view) {
        validator.validate();

    }

    public void changePasswordConfirm(){
        String token = confirmCode.getText().toString();
        String password = newPassword.getText().toString();
        new HttpRequestResetPassword().resetPasswordPost(token, password);
    }

    @Override
    public void onValidationSucceeded() {
        if(NetworkChecker.isOnline(cm)){
            changePasswordConfirm();
        }
        else{
            Toast.makeText(this, "Network isn´t available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
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
    public void onHttp(HttpResponseResetPassword response) {
        if(response.getCode() == 200){
            Intent intent = new Intent(ResetPasswordActivity.this, IcelandEvents.class);
            startActivity(intent);
        }
        Log.d("Gögn frá index", "tóskt: " +response.getCode());
    }
}
