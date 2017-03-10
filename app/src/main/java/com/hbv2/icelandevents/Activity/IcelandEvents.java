package com.hbv2.icelandevents.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hbv2.icelandevents.Adapter.EventAdapter;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
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
    private boolean signedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkUserInfo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iceland_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        eventListView = (ListView) findViewById(R.id.eventListView);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(signedIn){
            getMenuInflater().inflate(R.menu.menu_when_signed_in, menu);
            return true;
        }
        else {
            getMenuInflater().inflate(R.menu.menu_iceland_events, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sign_in) {
            signInBtnActOnClick();
            return true;
        }

        if (id == R.id.menu_sign_up){
           // signUpBtnActOnClick();
            return true;
        }

        if (id == R.id.menu_my_events){
            myEventsMenuBtn();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    // Allt hér fyrir neðan er eftir Martin.....:
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
    public void onHttp(HttpResponseEvent event) {
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(event.getCode() == 200){
        eventsList = event.getListEvent();
        updateDisplay();
        }
        Log.d("Gögn frá index", "tóskt");
    }


    private void requestEvents(){
        loadingDisplay.setVisibility(View.VISIBLE);
        new HttpRequestEvent().indexGet();
    }

    public  void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.event_layout, eventsList);
        eventListView.setAdapter(eventAdapter);
    }


    public void getEventBtn (View v){
        System.out.println("btnGetEvent");
         if(NetworkChecker.isOnline(cm)){
            requestEvents();
        }else{
            Toast.makeText(this, "Network isn't avilable",Toast.LENGTH_LONG).show();
        }

    }

    public void signInBtnActOnClick (){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signUpBtnActOnClick (View v){
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    public void myEventsMenuBtn (){
        Intent intent = new Intent(this, MyEventActivity.class);
        startActivity(intent);
    }



    private void checkUserInfo(){
        Log.d("checkUserInfo ","form inn í");
        if(!AutoLogin.checkUserInfo(this)){
            Intent intent = new Intent(this, LoginActivity.class);
            Event event = new Event();

            //intent.putExtra("nnns", (Parcelable) event);
            intent.putExtra("SKIP_VISIBLE", true);
            startActivity(intent);
        }
        else{
            signedIn = true;
        }

    }




}
