package com.hbv2.icelandevents.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.hbv2.icelandevents.HttpRequest.HttpRequestSignIn;
import com.hbv2.icelandevents.HttpResponse.HttpResponseSignIn;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.StoreUser;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;




public class LoginActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private TextView signInMsg;
    private Button skipBtn;
    private Button backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        signInMsg = (TextView) findViewById(R.id.signInMsgTextView);
        skipBtn = (Button) findViewById(R.id.skipBtnId);
        backBtn = (Button) findViewById(R.id.backSignInBtnId);
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
    public void onHttpLogin(HttpResponseSignIn event) {
        if(event.getCode() == 200){
            Log.d("Login :","rétt passwor og username");
            signInMsg.setText("Tókast að loga inn");
            StoreUser.storeUserInfo(username.getText().toString(),password.getText().toString(),this);
            finish();
        }
        else if (event.getCode() == 401) {
            Log.d("Login :", "Ekki rétt passwor eða username");
            signInMsg.setText("Tókast ekki að loga inn");
        }
    }

    public void skipBtnVisibility(){
        if(getIntent().getBooleanExtra("SKIP_VISIBLE",false)){
            skipBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void signInOnClick(View v){
        new HttpRequestSignIn().signInGet(username.getText().toString(),password.getText().toString());
    }

    public void skipOnClick(View v){
        StoreUser.skipUserInfo(this);
        finish();
    }

    public void backSignInBtnId(View v){
        finish();
    }

    public void forgotPasswBtn(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
