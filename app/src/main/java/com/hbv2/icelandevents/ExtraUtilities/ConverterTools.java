package com.hbv2.icelandevents.ExtraUtilities;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ekh on 13/03/2017.
 */

public class ConverterTools {
    public static String toDateFormat(String milliseconds){
        long value = Long.parseLong(milliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return sdf.format(new Date(value));
    }

    public static String toDateFormat(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static String toTimeFormat(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static Long toDay(){
        return System.currentTimeMillis() - 1000;
    }

    public static String toJson(Event event){
        return new Gson().toJson(event);
    }

    public static Event toEvent(String parsed){
        return new Gson().fromJson(parsed,Event.class);
    }
}
