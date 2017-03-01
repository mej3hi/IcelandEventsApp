package com.hbv2.icelandevents.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
import com.hbv2.icelandevents.R;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

public class CreateEventActivity extends AppCompatActivity implements Validator.ValidationListener{
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

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        eventName = (EditText) findViewById(R.id.eventNameText);
        eventLocation = (EditText) findViewById(R.id.locationText);
        eventDescription = (EditText) findViewById(R.id.descriptionText);
        eventTime = (EditText) findViewById(R.id.timeText);
        eventDate = (EditText) findViewById(R.id.dateText);

        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    public void upImageBtnOnclick(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            event.setImageurl(selectedImage.getPath());
        }
    }


    @Override
    public void onValidationSucceeded() {
        event = new Event();

        String name = eventName.getText().toString();
        event.setName(name);

        String location = eventLocation.getText().toString();
        event.setLocation(location);

        String description = eventDescription.getText().toString();
        event.setDescription(description);

        String time = eventTime.getText().toString();
        event.setName(time);

        String date = eventDate.getText().toString();
        event.setDate(date);

        new HttpRequestEvent().createEventPost(event, event.getImageurl());


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

    public void createBtnOnClick(View view) {
        validator.validate();
    }
}
