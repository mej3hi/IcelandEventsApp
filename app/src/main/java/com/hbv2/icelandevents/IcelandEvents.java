package com.hbv2.icelandevents;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbv2.icelandevents.API.EventAPI;
import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Adapter.EventAdapter;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.Service.ServiceGenerator;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IcelandEvents extends AppCompatActivity {
//bara prufa comment skuli
    private List<Event> eventsList;
    private ListView eventListView;
    private ProgressBar loadingDisplay;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iceland_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventListView = (ListView) findViewById(R.id.eventListView);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);


        checkUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Allt hér fyrir neðan er eftir Martin.....:

    private void requestEvents(){
        loadingDisplay.setVisibility(View.VISIBLE);

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());

                if(response.isSuccessful()){
                    eventsList = response.body();
                    updateDisplay();

                } else {
                    try {
                        System.out.println("Error :"+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loadingDisplay.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);


                loadingDisplay.setVisibility(View.INVISIBLE);
            }
        });
    }


   //lallsaldlas
    protected void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.event_layout, eventsList);
        eventListView.setAdapter(eventAdapter);
    }

    public void getEventBtn (View v){
        System.out.println("btnGetEvent");
        if(isOnline()){
            requestEvents();
        }else{
            Toast.makeText(this, "Network isn't avilable",Toast.LENGTH_LONG).show();
        }
    }

    public void signInOnClick (View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }


    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }


    private void checkUserInfo(){
        User user;
        Gson gson = new Gson();
        String filename = "userInfo";
        try {
            FileInputStream fin = openFileInput(filename);
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            if(temp != ""){
                Log.d("Temp ","Temp er ekki tómur");
                user = gson.fromJson(temp,User.class);

                if(user.getUsername() != "" && user.getPassword() != ""){
                    Log.d("Temp","Þá loga okku inn");
                    requestLogin(user.getUsername(),user.getPassword());

                }

                if(user.getUsername() == "" && user.getPassword() == ""){
                    Log.d("Temp","Þá loga okkur ekki inn");
                }
            }
            fin.close();
        }
        catch (Exception e){
            Log.d("tempErro","temp er ekki til");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }



    private void requestLogin(String username, String password){
        loadingDisplay.setVisibility(View.VISIBLE);

        UserAPI userAPI = ServiceGenerator.createService(UserAPI.class,username,password);
        Call<Void> call = userAPI.login();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());

                if(response.isSuccessful()) {
                    System.out.println("rétt passwor og username");
                }
                else if (response.code() == 401){
                    System.out.println("Ekki rétt passwor eða username");
                }else {
                    try {
                        System.out.println("Error :"+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loadingDisplay.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Failure :" +t);
                loadingDisplay.setVisibility(View.INVISIBLE);
            }
        });
    }

}
