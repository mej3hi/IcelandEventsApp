package com.hbv2.icelandevents.HttpRequest;


import android.util.Log;

import com.hbv2.icelandevents.API.EventAPI;
import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.HttpResponse.HttpEvent;
import com.hbv2.icelandevents.Service.ServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Martin on 29.1.2017.
 */


public class HttpRequestEvent {

    /**
     * Send Get method url ("/m/").
     * It get the main Event.
     */
     public void indexGet(){
        Log.d("indexController","það tókst");

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());

                EventBus.getDefault().post(new HttpEvent(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

            }
        });
    }

    /**
     * Send a Get method url ("/m/calander")
     * It finds all event to this day
     * @param day Is the day to look for.
     */
    public void calanderGet(String day){

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());

                //EventBus.getDefault().post(new HttpEvent(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

            }
        });
    }

    /**
     * Send a Post mapping url ("/m/createEvent")
     * It store event form that user has create to database.
     * @param event The value form the form
     * @param imageUri The path where the image is store
     */

    public void createEventPost(Event event, String imageUri){
        Log.d("indexController","það tókst");

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                //EventBus.getDefault().post(new HttpEvent(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

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
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                //EventBus.getDefault().post(new HttpEvent(response.body(),response.code()));
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
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                //EventBus.getDefault().post(new HttpEvent(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
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
        Log.d("indexController","það tókst");

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());
                //EventBus.getDefault().post(new HttpEvent(response.body(),response.code()));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);
                System.out.println("Failure call :" +call);

            }
        });
    }







}
