package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.hbv2.icelandevents.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText confirmCode;
    private EditText newPassword;
    private EditText confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        confirmCode = (EditText) findViewById(R.id.confirmCodeText);
        newPassword = (EditText) findViewById(R.id.newPasswText);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswText);

        Intent intent = getIntent();


    }

    public void changePasswOnClick(View view) {


    }
}
