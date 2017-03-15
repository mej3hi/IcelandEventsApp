package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestCall;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.Service.ServiceGenerator;
import com.hbv2.icelandevents.StoreUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;

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

    /**
     * Here we get the Respond from the backend server.
     * @param response Response has the Code and Msg from backend server.
     */
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

    /**
     * Here we make the skip Button visible if we get true from activity.
     */
    public void skipBtnVisibility(){
        if(getIntent().getBooleanExtra("SKIP_VISIBLE",false)){
            skipBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Here we send the Sign In from to the backend server and also check for
     * internet connection before sending it.
     */
    public void sendSignIn(){
        errorMsg.setText("");
        if(NetworkChecker.isOnline(this)){
            String u = username.getText().toString();
            String p = password.getText().toString();
            Call<String> call = ServiceGenerator.createService(UserAPI.class,u,p).getSignIn();
            HttpRequestCall.callReponseMsg(call);
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    /**
     * Her we listen to the Sign in Button for on click and
     *  If pressed, then we call sendSignIn method.
     * @param v v is the GUI components
     */
    public void signInOnClick(View v){
        sendSignIn();
    }

    /**
     * Her we listen to the Skip Button for on click.
     * If pressed, then we store that information on the phone
     * and go back to activity called Intent to this one.
     * @param v V is the GUI components.
     */
    public void skipOnClick(View v){
        StoreUser.skipUserInfo(this);
        finish();
    }

    /**
     * Her we listen to the ForgotPassword Button for on click.
     * If pressed, then we call on Intent for ForgotPasswordActivity.class
     * @param view View is the GUI components.
     */
    public void forgotPasswBtn(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
