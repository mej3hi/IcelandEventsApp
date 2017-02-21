package com.hbv2.icelandevents;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.HttpRequest.HttpRequestLogin;
import com.hbv2.icelandevents.HttpResponse.HttpLogin;



import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;




public class LoginActivity extends AppCompatActivity {

    User user = new User();
    private EditText username;
    private EditText password;
    private TextView signInMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        signInMsg = (TextView) findViewById(R.id.signInMsgTextView);

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
    public void onHttpLogin(HttpLogin event) {
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

    public void signInOnClick(View v){
        new HttpRequestLogin().loginGet(username.getText().toString(),password.getText().toString());
    }

    public void skipOnClick(View v){
        StoreUser.skipUserInfo();
        finish();
    }

}
