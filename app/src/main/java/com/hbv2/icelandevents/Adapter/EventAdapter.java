package com.hbv2.icelandevents.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.ExtraUtilities.ConverterTools;
import com.hbv2.icelandevents.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * It crate ArrayAdapter for the Event to display on screen on the phone 
 */

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> eventsList;

    public EventAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
        this.context = context;
        this.eventsList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.event_layout,parent,false);

        Event event = eventsList.get(position);

        TextView name = (TextView) view.findViewById(R.id.nameTextView);
        TextView date = (TextView) view.findViewById(R.id.dateTextView);
        TextView time = (TextView) view.findViewById(R.id.timeTextView);
        ImageView eventImg = (ImageView) view.findViewById(R.id.eventImageView);

        name.setText(event.getName());
        date.setText(ConverterTools.toDateFormat(event.getDate()));
        time.setText(event.getTime());
        Picasso.with(context).load(event.getImageurl()).into(eventImg);

        return view;
    }
}
