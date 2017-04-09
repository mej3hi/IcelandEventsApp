package com.hbv2.icelandevents.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        eventListView.setOnItemClickListener(itemClickListener);

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

    /**
     * Setting Click Listener for the calendarDate(EditText)
     * to show DatePickerDialog when it's clicked
     */
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
                DatePickerDialog dpd = new DatePickerDialog(IcelandEvents.this,date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(ConverterTools.toDay());
                dpd.show();
            }
        });
    }

    /**
     * Listener when event is clicked it directs user to DetailEventActivity
     */
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(IcelandEvents.this, DetailEventActivity.class);
            Event event = eventsList.get(position);
            String parsed = ConverterTools.toJson(event);
            intent.putExtra("EVENT_DETAIL",parsed);

            startActivity(intent);
        }
    };

    /**
     * Receiving the Respond from the backend server.
     * @param response Response has the Code and Message from backend server.
     */
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

    /**
     * If the user is signed in, signed in menu is shown
     */
    public void userSignedInMenu(){
        if(UserInfo.isLogin()){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_when_signed_in, menu);
            signInAs.setText("Signed in as : "+UserInfo.getUsername());
        }
    }

    /**
     * If the user is not signed in, a common menu is shown
     */
    public void userSignOutMenu(){
        if(!UserInfo.isLogin()){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_iceland_events, menu);
            signInAs.setText("");
        }
    }

    /**
     * Receiving the Respond from the backend server.
     * @param event Event has the Code and List<Event> from backend server
     */
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
            PopUpMsg.toastMsg("Something went wrong when trying to get the events",this);
        }
    }

    /**
     * Sends HttpRequest
     * Requesting the next 6 events.
     * Also checks for internet connection before sending it
     */
    private void requestEvents(){
        if(NetworkChecker.isOnline(this)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().indexGet();
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    /**
     * Sends HttpRequest containing a date
     * Requesting all events for that day.
     * Also checks for internet connection before sending it.
     * @param day Day is the day to look for
     */
    private void requestEventsByDate(String day){
        if(NetworkChecker.isOnline(this)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().calanderGet(day);
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    /**
     * Setting EventAdapter containing the event list to the eventListView
     */
    public  void updateDisplay(){
        EventAdapter eventAdapter = new EventAdapter(this, eventsList);
        eventListView.setAdapter(eventAdapter);
    }

    /**
     * Checks whether user is using the app for the first time,
     * if so user is directed to SignInActivity,
     * if not then user will be automatically logged in.
     */
    private void checkUserInfo(){
        if(!UserInfo.isLogin()) {
            if (!AutoLogin.checkUserInfo(this)) {
                Intent intent = new Intent(this, SignInActivity.class);
                intent.putExtra("SKIP_VISIBLE", true);
                startActivity(intent);
            }
        }
    }

    /**
     * Setting click listener to the mainTitle
     * when clicked calls the requestEvents() method
     */
    private void setListener(){
        mainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestEvents();
            }
        });
    }

}
