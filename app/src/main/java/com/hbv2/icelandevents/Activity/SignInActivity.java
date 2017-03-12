package com.hbv2.icelandevents.Activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.hbv2.icelandevents.HttpRequest.HttpRequestSignIn;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.HttpResponse.HttpResponseSignIn;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.StoreUser;
import com.mobsandgeeks.saripaar.Validator;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;




public class SignInActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private TextView errorMsg;
    private Button skipBtn;
    private Button backBtn;
    private ConnectivityManager cm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public void onSignIn(HttpResponseMsg msg){
        if(msg.getCode() == 200){
            Toast toast = Toast.makeText(this,"Sign In Success", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            StoreUser.storeUserInfo(username.getText().toString(),password.getText().toString(),this);
            finish();
        }
        else if(msg.getCode() == 401 ){
            errorMsg.setText("Username or Password are wrong");
        }
        else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Something went wrong");
            alertDialogBuilder
                    .setMessage("Something went wrong with the Sign in, please try again")
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

    public void skipBtnVisibility(){
        if(getIntent().getBooleanExtra("SKIP_VISIBLE",false)){
            skipBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void signInOnClick(View v){
        errorMsg.setText("");
        if(NetworkChecker.isOnline(cm)){
            new HttpRequestSignIn().signInGet(username.getText().toString(),password.getText().toString());
        }else{
            Toast toast = Toast.makeText(this,"Network isn't available",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
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
