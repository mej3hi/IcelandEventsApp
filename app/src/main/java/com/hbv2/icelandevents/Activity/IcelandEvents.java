package com.hbv2.icelandevents.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hbv2.icelandevents.Adapter.EventAdapter;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.AutoLogin;
import com.hbv2.icelandevents.Service.NetworkChecker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class IcelandEvents extends AppCompatActivity {

    private List<Event> eventsList;
    private ListView eventListView;
    private ProgressBar loadingDisplay;
    private ConnectivityManager cm;
    private Menu menu;
    private TextView signInAs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iceland_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventListView = (ListView) findViewById(R.id.eventListView);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        signInAs = (TextView) findViewById(R.id.signInAsIdTextView);
        signInAs.setText("Sign In As : ");

        requestEvents();
        checkUserInfo();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_iceland_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sign_in) {
            signInBtnMenuBtn();
            return true;
        }

        if (id == R.id.menu_sign_up){
            signUpBtnMenuBtn();
            return true;
        }

        if (id == R.id.menu_my_events){
            myEventsMenuBtn();
            return true;
        }

        if (id == R.id.menu_sign_out){
            UserInfo.setLogin(false);
            userSignOutMenu();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        userSignedInMenu();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void onAutoLogin(HttpResponseMsg response){
        if(response.getCode() == 200){
            UserInfo.setLogin(true);
            userSignedInMenu();
        }
        else{
            UserInfo.setLogin(false);
            toastMsg("Could not auto login");
        }
    }

    public void userSignedInMenu(){
        if(UserInfo.isLogin()){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_when_signed_in, menu);
            signInAs.setText("Sign In As : "+UserInfo.getLoginUsername());
        }
    }

    public void userSignOutMenu(){
        Log.d("listinn: ",""+eventsList.size());
//        if(!UserInfo.isLogin()){
//            menu.clear();
//            getMenuInflater().inflate(R.menu.menu_iceland_events, menu);
//            signInAs.setText("Sign In As : ");
//        }
    }


    @Subscribe
    public void onIndexEvent(HttpResponseEvent event) {
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(event.getCode() == 200){
        eventsList = event.getListEvent();
        updateDisplay();
        }
        else {
            toastMsg("Something went wrong trying get the Event");
        }
    }

    private void requestEvents(){
        if(NetworkChecker.isOnline(cm)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().indexGet();
        }else{
            toastMsg("Cannot get Event no network isn't available");
        }
    }

    public  void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.event_layout, eventsList);
        eventListView.setAdapter(eventAdapter);
    }

    public void signInBtnMenuBtn (){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void signUpBtnMenuBtn (){
        Intent intent = new Intent(this, MyEventActivity.class);
        startActivity(intent);
    }

    public void myEventsMenuBtn (){
        Intent intent = new Intent(this, MyEventActivity.class);
        startActivity(intent);
    }

    private void checkUserInfo(){
        Log.d("checkUserInfo ","form inn í");

        if(!NetworkChecker.isOnline(cm)) {
            toastMsg("Cannot autoLogin network isn't available");
        }

        if(!AutoLogin.checkUserInfo(this)) {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.putExtra("SKIP_VISIBLE", true);
            startActivity(intent);
        }
    }

    public void toastMsg(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
