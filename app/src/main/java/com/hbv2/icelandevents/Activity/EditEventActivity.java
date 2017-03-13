package com.hbv2.icelandevents.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity implements Validator.ValidationListener{
    Event event;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, maxLength = 32, message = "Please use between 3 and 32 characters.")
    EditText eventName;

    @Required(order = 3)
    @TextRule(order = 4, minLength = 3, maxLength = 32, message = "Please use between 3 and 32 characters.")
    EditText eventLocation;

    @Required(order = 6)
    @TextRule(order = 7, minLength = 3, maxLength = 250, message = "Please use between 3 and 250 characters.")
    EditText eventDescription;

    @Required(order = 8)
    @Regex( order = 9, pattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "Please use hh:mm with 24 format.")
    EditText eventTime;

    @Required(order = 10)
    EditText eventDate;

    TextView imageUrl;

    Validator validator;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventName = (EditText) findViewById(R.id.eventNameText);
        eventLocation = (EditText) findViewById(R.id.locationText);
        eventDescription = (EditText) findViewById(R.id.descriptionText);
        eventTime = (EditText) findViewById(R.id.timeText);
        eventDate = (EditText) findViewById(R.id.dateText);
        imageUrl = (TextView) findViewById(R.id.imageUrlText);

        validator = new Validator(this);
        validator.setValidationListener(this);

        getEvent();
        setFields();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditEventActivity.this,date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time =  new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateFieldTime();
            }
        };

        eventTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditEventActivity.this,time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
            }
        });
    }

    private void getEvent(){
        String parsed = getIntent().getStringExtra("EVENT_NAME");
        Gson gson = new Gson();
        event = gson.fromJson(parsed,Event.class);
        Log.d("EventName: ", event.getName());
    }

    private void setFields(){
        eventName.setText(event.getName());
        eventLocation.setText(event.getLocation());
        eventDescription.setText(event.getDescription());
        eventTime.setText(event.getTime());
        eventDate.setText(toDateFormat(event.getDate()));
        imageUrl.setText(event.getImageurl().replaceAll(".+/(.*)$","$1"));
        radioButtonSet(event.getMusicgenres());
    }

    private void updateFieldTime(){
        String timeFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.US);
        eventTime.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        eventDate.setText(sdf.format(calendar.getTime()));
    }

    public void upImageBtnOnclick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            android.net.Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            event.setImageurl(filePath);
            imageUrl.setText(filePath);
        }
    }

    @Override
    public void onValidationSucceeded() {
        String name = eventName.getText().toString();
        event.setName(name);

        String location = eventLocation.getText().toString();
        event.setLocation(location);

        String description = eventDescription.getText().toString();
        event.setDescription(description);

        String time = eventTime.getText().toString();
        event.setTime(time);

        String date = eventDate.getText().toString();
        event.setDate(date);

        event.setMusicgenres(getRadioBtnValue());

        new HttpRequestEvent().editEventPost(event, event.getImageurl());
    }

    public void onValidationFailed(View view, Rule<?> rule) {
        final String failureMessage = rule.getFailureMessage();
        if (view instanceof EditText) {
            EditText failed = (EditText) view;
            failed.requestFocus();
            failed.setError(failureMessage);
        } else {
            Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveBtnOnClick(View view) {
        validator.validate();
    }


    public void removeBtnOnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you really want to remove this event?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeEvent();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void removeEvent(){
        new HttpRequestEvent().removeEventGet(event.getId());
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioBtnOther:
                if (checked)
                    event.setMusicgenres("Other");
                break;
            case R.id.radioBtnRock:
                if (checked)
                    event.setMusicgenres("Rock");
                break;
            case R.id.radioBtnPop:
                if (checked)
                    event.setMusicgenres("Pop");
                break;
            case R.id.radioBtnJazz:
                if (checked)
                    event.setMusicgenres("Jazz");
                break;
        }

    }

    private void radioButtonSet(String genre){
        switch(genre) {
            case "Other":
                ((RadioButton) findViewById(R.id.radioBtnOther)).setChecked(true);
                break;
            case "Rock":
                ((RadioButton) findViewById(R.id.radioBtnRock)).setChecked(true);
                break;
            case "Pop":
                ((RadioButton) findViewById(R.id.radioBtnPop)).setChecked(true);
                break;
            case "Jazz":
                ((RadioButton) findViewById(R.id.radioBtnJazz)).setChecked(true);
                break;
        }
    }

    private String getRadioBtnValue(){
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton selected = (RadioButton)findViewById(group.getCheckedRadioButtonId());
        return selected.getText().toString();
    }

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
    public void onEditEvent(HttpResponseMsg response){
        if(response.getCode() == 200){
            Toast.makeText(getApplicationContext(), response.getMsg(), Toast.LENGTH_LONG).show();
            goBack();
        }
    }

    private void goBack(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",true);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private String toDateFormat(String milliseconds){
        long value = Long.parseLong(milliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        return sdf.format(new Date(value));
    }
}
