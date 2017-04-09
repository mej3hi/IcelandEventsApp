package com.hbv2.icelandevents.HttpRequest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbv2.icelandevents.API.EventAPI;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Martin on 29.1.2017.
 */


public class HttpRequestEvent {


    /**
     * Sends Get method url ("/m/").
     * for getting the music events.
     */
    public void indexGet(){
        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getIndex();
        HttpRequestCall.callResponseEvent(call);
    }

    /**
     * Sends Get method url ("/m/calander")
     * for getting all music events for this day.
     * @param day Is the day to look for.
     */
    public void calanderGet(String day){
        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getCalander(day);
        HttpRequestCall.callResponseEvent(call);
    }


    /**
     * Sends Post mapping url ("/m/createEvent")
     * for storing the event which the user has created to database.
     * @param event the event that is to be stored
     * @param imageUri The path where the image is stored
     */
    public void createEventPost(Event event, String imageUri){
        File file = new File(imageUri);

        ByteArrayOutputStream outStream = scaleImg(file);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), outStream.toByteArray());
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("name", toRequestBody(event.getName()));
        map.put("location", toRequestBody(event.getLocation()));
        map.put("description", toRequestBody(event.getDescription()));
        map.put("time", toRequestBody(event.getTime()));
        map.put("date", toRequestBody(event.getDate()));
        map.put("musicgenres", toRequestBody(event.getMusicgenres()));

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<String> call = eventAPI.postCreateEvent(body,map);
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Sends Get method url ("/m/myevents")
     * for getting all the music events from that user.
     */
    public void myEventGet(){
        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getMyEvents();
        HttpRequestCall.callResponseEvent(call);
    }

    /**
     * Sends Get method url ("/m/removeEvent")
     * for removing an event from the user.
     * @param id Is the ID of event
     */
    public void removeEventGet(Long id){
        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<String> call = eventAPI.getRemoveEvent(id);
        HttpRequestCall.callReponseMsg(call);
    }

    /**
     * Send Post method url ("/m/editEvent")
     * for updating the edited event from the user in database.
     * @param event the event that is to be updated
     * @param imageUri The path where the image is stored
     */
    public void editEventPost(Event event,String imageUri){
        Map<String, RequestBody> map = new HashMap<>();
        map.put("id",toRequestBody(""+event.getId()));
        map.put("name", toRequestBody(event.getName()));
        map.put("location", toRequestBody(event.getLocation()));
        map.put("description", toRequestBody(event.getDescription()));
        map.put("time", toRequestBody(event.getTime()));
        map.put("date", toRequestBody(event.getDate()));
        map.put("musicgenres", toRequestBody(event.getMusicgenres()));
        map.put("imageurl", toRequestBody(event.getImageurl()));


        File file = new File(imageUri);
        RequestBody reqFile;

        if(imageUri.matches("http.*"))
            reqFile = RequestBody.create(MediaType.parse("image/*"), "");
        else{
            ByteArrayOutputStream outStream = scaleImg(file);
            reqFile = RequestBody.create(MediaType.parse("image/*"), outStream.toByteArray());
        }

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<String> call = eventAPI.postEditEvent(body, map);
        HttpRequestCall.callReponseMsg(call);
    }


    private RequestBody toRequestBody (String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    /**
     * @param file the image file
     * @return Bitmap outStream
     */
    private ByteArrayOutputStream scaleImg(File file){
        Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
        int destWidth = 640;
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        double rate = (double)origWidth /(double)destWidth;
        int destHeight =(int)( origHeight / rate);
        // we create an scaled bitmap so it reduces the image, not just trim it
        Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
        // compress to the format you want, JPEG, PNG...
        // 70 is the 0-100 quality percentage
        b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        return outStream;
    }
}
