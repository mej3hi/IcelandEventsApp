package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Adapter.EventAdapter;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.NetworkChecker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MyEventActivity extends AppCompatActivity {
    private List<Event> eventsList;
    private ListView eventListView;
    private ProgressBar loadingDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventListView = (ListView) findViewById(R.id.eventListViewMe);
        eventListView.setOnItemClickListener(itemClickListener);

        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayME);
        loadingDisplay.setVisibility(View.INVISIBLE);

        TextView signInAs = (TextView) findViewById(R.id.signInAsIdTextView);
        signInAs.setText("Signed in as : "+UserInfo.getUsername());
    }


    /**
     * Listener when event is clicked that directs user to EditEventActivity
     */
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyEventActivity.this, EditEventActivity.class);
            Event event = eventsList.get(position);
            Gson gson = new Gson();
            String parsed = gson.toJson(event);
            intent.putExtra("EVENT_NAME",parsed);

            startActivity(intent);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_event) {
            createEventMenuBtn();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        requestEvents();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Receiving Respond from the backend server.
     * @param response Response has the Code and the Msg from backend server.
     */
    @Subscribe
    public void onMyEvent(HttpResponseEvent response) {
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(response.getCode() == 200){
            eventsList = response.getListEvent();
            updateDisplay();
        }else if(response.getCode() == 401) {
            PopUpMsg.toastMsg("Your session has expired, please sign in", this);
            redirectToSignIn();
        }else
            PopUpMsg.toastMsg("Something went wrong, please try again", this);
    }

    private  void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, eventsList);
        eventListView.setAdapter(eventAdapter);
    }

    /**
     * Sends HttpRequest that request all Events from user
     */
    private void requestEvents() {
        if(NetworkChecker.isOnline(this)){
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().myEventGet();
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    private void createEventMenuBtn(){
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    private void redirectToSignIn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}
