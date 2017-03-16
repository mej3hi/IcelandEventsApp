package com.hbv2.icelandevents.Adapter;



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

import java.util.ArrayList;
import java.util.List;

/**
 * It crate ArrayAdapter for the Event to display on screen on the phone 
 */

public class EventAdapter extends ArrayAdapter<Event> {

    private static class ViewHolder {
        TextView name;
        TextView date ;
        TextView time ;
        TextView description;
        TextView location;
        TextView musicgenres;
        ImageView eventImg;
    }


    private Context context;
    private List<Event> eventsList;
    public EventAdapter(Context context, List<Event> event) {
        super(context, 0, event);
        this.context = context;
        this.eventsList =  event;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event event = eventsList.get(position);

        ViewHolder viewHolder;

        if(convertView==null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView  =inflater.inflate(R.layout.event_layout, parent,false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.time = (TextView) convertView.findViewById(R.id.timeTextView);
            viewHolder.description =(TextView) convertView.findViewById(R.id.descTextViewId);
            viewHolder.location =(TextView) convertView.findViewById(R.id.locaTextViewId);
            viewHolder.musicgenres =(TextView) convertView.findViewById(R.id.musicTypeTextViewId);
            viewHolder.eventImg = (ImageView) convertView.findViewById(R.id.eventImageView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder =(ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(event.getName());
        viewHolder.date.setText(ConverterTools.toDateFormat(event.getDate()));
        viewHolder.time.setText(event.getTime());
        viewHolder.description.setText(event.getDescription());
        viewHolder.location.setText(event.getLocation());
        viewHolder.musicgenres.setText("Type : "+event.getMusicgenres());
        Picasso.with(context).load(event.getImageurl()).into(viewHolder.eventImg);

        return convertView;
    }

}
