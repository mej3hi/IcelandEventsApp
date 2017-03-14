package com.hbv2.icelandevents.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.hbv2.icelandevents.Adapter.EventAdapter;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.ExtraUtilities.ConverterTools;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.hbv2.icelandevents.Service.AutoLogin;
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.hbv2.icelandevents.StoreUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;



public class IcelandEvents extends AppCompatActivity {

    private List<Event> eventsList;
    private ListView eventListView;
    private ProgressBar loadingDisplay;
    private Menu menu;
    private TextView signInAs;
    private EditText calendarDate;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private TextView mainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iceland_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainTitle = (TextView) toolbar.getChildAt(0);
        eventListView = (ListView) findViewById(R.id.eventListView);
        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayPB);
        loadingDisplay.setVisibility(View.INVISIBLE);
        signInAs = (TextView) findViewById(R.id.signInAsIdTextView);
        calendarDate = (EditText) findViewById(R.id.calendarEditTextId);

        UserInfo.setLogin(false);
        setDateTimeField();
        setListener();

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
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_sign_up){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_my_events){
            Intent intent = new Intent(this, MyEventActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_sign_out){
            StoreUser.skipUserInfo(this);
            userSignOutMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        requestEvents();
        userSignedInMenu();
        checkUserInfo();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private void setDateTimeField(){
        calendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                calendarDate.setText(ConverterTools.toDateFormat(calendar));
                requestEventsByDate(calendarDate.getText().toString());
            }
        };

        calendarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(IcelandEvents.this,date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Subscribe
    public void onAutoLogin(HttpResponseMsg response){
        if(response.getCode() == 200){
            UserInfo.setLogin(true);
            userSignedInMenu();
        }
        else{
            PopUpMsg.toastMsg("Could not auto login",this);
        }
    }

    public void userSignedInMenu(){
        if(UserInfo.isLogin()){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_when_signed_in, menu);
            signInAs.setText("Signed in as : "+UserInfo.getUsername());
        }
    }

    public void userSignOutMenu(){
        if(!UserInfo.isLogin()){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_iceland_events, menu);
            signInAs.setText("");
        }
    }

    @Subscribe
    public void onEvent(HttpResponseEvent event) {
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(event.getCode() == 200){
            eventsList = event.getListEvent();

            if(eventsList.isEmpty()){
                PopUpMsg.toastMsg("No events were found",this);
                calendarDate.setText("");
            }else {
                updateDisplay();
            }
        }
        else {
            PopUpMsg.toastMsg("Something went wrong trying get the Event",this);
        }
    }

    private void requestEvents(){
        if(NetworkChecker.isOnline(this)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().indexGet();
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    private void requestEventsByDate(String day){
        if(NetworkChecker.isOnline(this)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().calanderGet(day);
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    public  void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.event_layout, eventsList);
        eventListView.setAdapter(eventAdapter);
    }

    private void checkUserInfo(){
        if(!NetworkChecker.isOnline(this)) {
            PopUpMsg.toastMsg("Cannot autoLogin network isn't available",this);
        }

        if(!UserInfo.isLogin()) {
            if (!AutoLogin.checkUserInfo(this)) {
                Intent intent = new Intent(this, SignInActivity.class);
                intent.putExtra("SKIP_VISIBLE", true);
                startActivity(intent);
            }
        }
    }

    private void setListener(){
        mainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestEvents();
            }
        });
    }

}
