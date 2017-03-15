package com.hbv2.icelandevents.HttpRequest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hbv2.icelandevents.API.EventAPI;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 29.1.2017.
 */


public class HttpRequestEvent {



    /**
     * Send a Post mapping url ("/m/createEvent")
     * It store event form that user has create to database.
     * @param event The value form the form
     * @param imageUri The path where the image is store
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

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseMsg(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);
                EventBus.getDefault().post(new HttpResponseMsg("",500));
            }
        });
    }

    /**
     * Send Get method url ("/m/myevents")
     * It finds all events that user has made.
     */
    public void myEventGet(){
        Log.d("indexController","það tókst");

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getMyEvents();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseEvent(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

            }
        });
    }

    /**
     * Send Get method url ("/m/removeEvent")
     * It allow user to remove his own events.
     * @param id Is the ID of event
     */
    public void removeEventGet(Long id){
        Log.d("indexController","það tókst");

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<String> call = eventAPI.getRemoveEvent(id);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseMsg(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

            }
        });
    }

    /**
     * Send Post method url ("/m/editEvent").
     * It store event form that user has edit to database.
     * @param event The value form the form
     * @param imageUri The path where the image is store
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

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                EventBus.getDefault().post(new HttpResponseMsg(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

            }
        });
    }





    private RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    private ByteArrayOutputStream scaleImg(File file){
        Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
        int destWidth = 640;
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        double a = (double)origWidth /(double)destWidth;
        int destHeight =(int)( origHeight / a);
        // we create an scaled bitmap so it reduces the image, not just trim it
        Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
        // compress to the format you want, JPEG, PNG...
        // 70 is the 0-100 quality percentage
        b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        return outStream;
    }


}
