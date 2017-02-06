package com.hbv2.icelandevents;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.User;


import java.io.FileOutputStream;





public class LoginActivity extends AppCompatActivity {

    User user = new User();
   private EditText username;
   private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

    }


    public void signInOnClick(View v){
        Gson gson = new Gson();
        user.setPassword(password.getText().toString());
        user.setUsername(username.getText().toString());
        String u = gson.toJson(user);
        Log.d("user",u);

        String filename = "userInfo";
        String string = u;
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void skipOnClick(View v){
        Gson gson = new Gson();
        user.setPassword("");
        user.setUsername("");
        String u = gson.toJson(user);
        Log.d("user",u);

        String filename = "userInfo";
        String string = u;
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
