package com.hbv2.icelandevents.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestSignUp;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.StoreUser;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
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
    @TextRule(order = 4, minLength = 6, maxLength = 32, message = "Please use between 6 and 32 characters.")
    @Email(order = 5, message= "Please enter a valid email address.")
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
    public void onSignUp(HttpResponseMsg response){
        if(response.getCode() == 200 && response.getMsg().equals("ok")){
            PopUpMsg.toastMsg("Sign Up Success",this);
            StoreUser.storeUserInfo(username.getText().toString(),password.getText().toString(),this);
            finish();
        }
        else if(response.getCode() == 200 && response.getMsg().equals("username_exists")) {
            EditText failed = username;
            failed.requestFocus();
            failed.setError("Username already exists");
        }
        else{
            String title ="Something went wrong";
            String msg = "Something went wrong with the Sign up, please try again";
            PopUpMsg.dialogMsg(title,msg,this);
        }
    }

    public void sendSignUp(){
        if(NetworkChecker.isOnline(this)){
            User user = new User();
            user.setName(name.getText().toString());
            user.setEmail(email.getText().toString());
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setPasswordConfirm(passwordConf.getText().toString());
            new HttpRequestSignUp().signUpPost(user);
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    @Override
    public void onValidationSucceeded() {
        sendSignUp();
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
            validator.validate();
    }


}
