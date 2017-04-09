package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestUser;
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
    private ProgressBar loadingDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);
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

    /**
     * Click listener for Sign in button when clicked
     * calls sendSignIn() method.
     * @param v v is the GUI component
     */
    public void signInOnClick(View v){
        sendSignIn();
    }

    /**
     * Sends HttpRequest containing username and password,
     * Requesting user to be signed in.
     * Also checks for internet connection before sending it.
     */
    public void sendSignIn(){
        errorMsg.setText("");
        if(NetworkChecker.isOnline(this)){
            loadingDisplay.setVisibility(View.VISIBLE);
            String userName = username.getText().toString();
            String passWord = password.getText().toString();;
            new HttpRequestUser().signInGet(userName,passWord);
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    /**
     * Receiving the Respond from the backend server.
     * @param response Response has the Code and Message from backend server.
     */
    @Subscribe
    public void onSignIn(HttpResponseMsg response){
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(response.getCode() == 200 && response.getMsg().equals("ok")){
            PopUpMsg.toastMsg("Sign In Success",this);
            StoreUser.storeUserInfo(username.getText().toString(),password.getText().toString(),true,this);
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

    /**
     * Skip Button is made visible when TRUE(boolean value)
     * is passed to this activity from another activity.
     */
    public void skipBtnVisibility(){
        if(getIntent().getBooleanExtra("SKIP_VISIBLE",false)){
            skipBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Click listener for Skip button when clicked
     * it stores that information on the phone
     * and directs user to the IcelandEvents activity.
     * @param v V is the GUI component
     */
    public void skipOnClick(View v){
        StoreUser.skipUserInfo(this);
        finish();
    }

    /**
     * Click listener for Forgot password Button when clicked
     * it directs user to the ForgotPasswordActivity.
     * @param view View is the GUI component
     */
    public void forgotPasswordBtn(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
