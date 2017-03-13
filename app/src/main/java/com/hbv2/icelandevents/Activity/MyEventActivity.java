package com.hbv2.icelandevents.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Adapter.EventAdapter;
import com.hbv2.icelandevents.Entities.Event;
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
    private ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventListView = (ListView) findViewById(R.id.eventListViewMe);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayME);
        loadingDisplay.setVisibility(View.INVISIBLE);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyEventActivity.this, EditEventActivity.class);
                Event event = eventsList.get(position);
                Gson gson = new Gson();
                String parsed = gson.toJson(event);
                intent.putExtra("EVENT_NAME",parsed);

                startActivityForResult(intent,1728);
            }
        });
    }
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
        getEvents();

    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    private void requestEvents(){
        loadingDisplay.setVisibility(View.VISIBLE);
        new HttpRequestEvent().myEventGet();
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

    public  void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.event_layout, eventsList);
        eventListView.setAdapter(eventAdapter);
    }


    public void getEvents() {
        System.out.println("btnGetEvent");
        if(NetworkChecker.isOnline(this)){
            requestEvents();
        }else{
            Toast.makeText(this, "Network isn't avilable",Toast.LENGTH_LONG).show();
        }
    }

    private void createEventMenuBtn(){
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1728){
            if(resultCode == RESULT_OK){
                if(data.getBooleanExtra("result",false))
                    getEvents();
            }
        }
    }
}
