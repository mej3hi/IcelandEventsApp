package com.hbv2.icelandevents.ExtraUtilities;

import java.text.SimpleDateFormat;
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
}
