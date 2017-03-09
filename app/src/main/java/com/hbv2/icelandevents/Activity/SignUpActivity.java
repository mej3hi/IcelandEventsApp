package com.hbv2.icelandevents.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.HttpRequest.HttpRequestSignUp;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.EventBus;


public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Validator validator;
    private User user;
    private ConnectivityManager cm;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    EditText name;

    @Required(order = 3)
    @TextRule(order = 4, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    EditText email;

    @Required(order = 5)
    @TextRule(order = 6, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    EditText username;

    @Required(order = 7)
    @Password(order = 8)
    @TextRule(order = 9, minLength = 8, maxLength = 32, message = "Please use between 6 and 32 characters.")
    EditText password;

    @Required(order = 10)
    @ConfirmPassword(order = 11)
    EditText passwordConf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        name = (EditText) findViewById(R.id.nameEditTextId);
        email = (EditText) findViewById(R.id.emailEditTextId);
        username = (EditText) findViewById(R.id.usernameEditTextId);
        password = (EditText) findViewById(R.id.passwordEditTextId);
        passwordConf = (EditText) findViewById(R.id.passwordConfEditTextId);


        validator = new Validator(this);
        validator.setValidationListener(this);

        user = new User();

    }

    /*@Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }*/


    @Override
    public void onValidationSucceeded() {
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setPasswordConfirm(passwordConf.getText().toString());
        new HttpRequestSignUp().signUpPost(user);
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


    public void signUpBtnOnClick (View v){
        if(NetworkChecker.isOnline(cm)){
            validator.validate();
        }else{
            Toast.makeText(this, "Network isn't avilable",Toast.LENGTH_LONG).show();
        }
    }






}
