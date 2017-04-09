package com.hbv2.icelandevents.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.ExtraUtilities.ConverterTools;
import com.hbv2.icelandevents.R;
import com.squareup.picasso.Picasso;

public class DetailEventActivity extends AppCompatActivity {
    Event event;
    ImageView eventImg;
    TextView date;
    TextView time;
    TextView location;
    TextView musicgenres;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventImg = (ImageView) findViewById(R.id.eventImageView);
        date = (TextView) findViewById(R.id.dateText);
        time = (TextView) findViewById(R.id.timeText);
        location = (TextView) findViewById(R.id.locationText);
        musicgenres = (TextView) findViewById(R.id.musicgenresText);
        description = (TextView) findViewById(R.id.descriptionText);
        getEvent();
        setFields();
    }

    /**
     * Gets the event which user clicked in MyEventActivity.
     */
    private void getEvent(){
        String parsed = getIntent().getStringExtra("EVENT_DETAIL");
        event = ConverterTools.toEvent(parsed);
    }

    /**
     * Sets the fields with values from the retrieved event.
     */
    private void setFields(){
        setTitle(event.getName());
        Picasso.with(this).load(event.getImageurl()).into(eventImg);
        date.setText(ConverterTools.toDateFormat(event.getDate()));
        time.setText(event.getTime());
        location.setText(event.getLocation());
        musicgenres.setText(event.getMusicgenres());
        description.setText(event.getDescription());
    }

}
