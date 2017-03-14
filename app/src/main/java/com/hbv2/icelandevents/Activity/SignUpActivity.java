package com.hbv2.icelandevents.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.HttpRequest.HttpRequestSignUp;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.StoreUser;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Validator validator;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    private EditText name;

    @Required(order = 3)
    @Regex(order = 4, pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
                      message = "Please use format email@exmple.com.")
    @TextRule(order = 5, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    private EditText email;

    @Required(order = 6)
    @TextRule(order = 7, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    private EditText username;

    @Required(order = 8)
    @Password(order = 9)
    @TextRule(order = 10, minLength = 8, maxLength = 32, message = "Please use between 6 and 32 characters.")
    private EditText password;

    @Required(order = 11)
    @ConfirmPassword(order = 12)
    private EditText passwordConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        name = (EditText) findViewById(R.id.nameEditTextId);
        email = (EditText) findViewById(R.id.emailEditTextId);
        username = (EditText) findViewById(R.id.usernameEditTextId);
        password = (EditText) findViewById(R.id.passwordEditTextId);
        passwordConf = (EditText) findViewById(R.id.passwordConfEditTextId);

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

    @Subscribe
    public void onSignUp(HttpResponseMsg msg){
        if(msg.getCode() == 200 && msg.getMsg().equals("ok")){
            Toast toast = Toast.makeText(this,"Sign Up Success", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            StoreUser.storeUserInfo(username.getText().toString(),password.getText().toString(),this);
            finish();
        }
        else if(msg.getCode() == 200 && msg.getMsg().equals("username_exists")) {
            EditText failed = username;
            failed.requestFocus();
            failed.setError("Username already exists");
        }
        else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Something went wrong");
            alertDialogBuilder
                .setMessage("Something went wrong with the Sign up, please try again")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
    }


    @Override
    public void onValidationSucceeded() {
        User user = new User();
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
        if(NetworkChecker.isOnline(this)){
            validator.validate();
        }else{
            Toast toast = Toast.makeText(this, "Network isn't available",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }






}
