package com.hbv2.icelandevents;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.Service.ServiceGenerator;


import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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


    public void signInOnClick(View v){
        requestLogin(username.getText().toString(),password.getText().toString());
    }

    public void skipOnClick(View v){
        skipUserInfo();
        finish();
    }

    private void requestLogin(final String username, final String password){


        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class,username,password);
        Call<Void> call = userAPI.login();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("response raw: ", String.valueOf(response.raw()));
                Log.d("response header:  " , String.valueOf(response.headers()));

                if(response.isSuccessful()) {
                    Log.d("Login :","rétt passwor og username");
                    signInMsg.setText("Tókast að loga inn");
                    storeUserInfo(username,password);
                    finish();

                }
                else if (response.code() == 401){
                    Log.d("Login :","Ekki rétt passwor eða username");
                    signInMsg.setText("Tókast ekki að loga inn");
                }else {
                    try {
                        Log.d("Error :", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Failure :", String.valueOf(t));

            }
        });
    }


    private void storeUserInfo(String username,String password){
        Gson gson = new Gson();
        user.setPassword(password);
        user.setUsername(username);
        String u = gson.toJson(user);
        Log.d("user :",u);

        String filename = "userInfo";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(u.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void skipUserInfo(){
        Gson gson = new Gson();
        user.setPassword("");
        user.setUsername("");
        String u = gson.toJson(user);
        Log.d("user",u);

        String filename = "userInfo";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(u.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
