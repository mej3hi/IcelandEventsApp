package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestSignIn;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.StoreUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SignInActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView errorMsg;
    private Button skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        errorMsg = (TextView) findViewById(R.id.errorMsgTextViewId);
        skipBtn = (Button) findViewById(R.id.skipBtnId);
        skipBtnVisibility();
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
    public void onSignIn(HttpResponseMsg response){
        if(response.getCode() == 200 && response.getMsg().equals("ok")){
            PopUpMsg.toastMsg("Sign In Success",this);
            StoreUser.storeUserInfo(username.getText().toString(),password.getText().toString(),this);
            finish();
        }
        else if(response.getCode() == 401 ){
            errorMsg.setText("Username or Password are wrong");
        }
        else{
            String title = "Something went wrong";
            String msg = "Something went wrong with the Sign in, please try again";
            PopUpMsg.dialogMsg(title,msg,this);
        }
    }

    public void skipBtnVisibility(){
        if(getIntent().getBooleanExtra("SKIP_VISIBLE",false)){
            skipBtn.setVisibility(View.VISIBLE);
        }
    }

    public void sendSignIn(){
        errorMsg.setText("");
        if(NetworkChecker.isOnline(this)){
            new HttpRequestSignIn().signInGet(username.getText().toString(),password.getText().toString());
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    public void signInOnClick(View v){
        sendSignIn();
    }

    public void skipOnClick(View v){
        StoreUser.skipUserInfo(this);
        finish();
    }

    public void forgotPasswBtn(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
