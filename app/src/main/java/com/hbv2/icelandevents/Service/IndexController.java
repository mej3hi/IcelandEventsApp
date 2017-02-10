package com.hbv2.icelandevents.Service;


import android.util.Log;

import com.hbv2.icelandevents.API.EventAPI;
import com.hbv2.icelandevents.Entities.Event;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 29.1.2017.
 */


public class IndexController {


    Http http = new Http();
    List<Event> e;
     public Http getIndexController(){

        Log.d("indexController","það tókst");

        EventAPI eventAPI = ServiceGenerator.createService(EventAPI.class);
        Call<List<Event>> call = eventAPI.getEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                System.out.println("response raw: " + response.raw());
                System.out.println("response header:  " + response.headers());

                if(response.isSuccessful()){
                    e = response.body();
                    System.out.println("Stæarð http" +e.size());




                } else {
                    try {
                        System.out.println("Error :"+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                System.out.println("Failure :" +t);

            }
        });
        //System.out.println("Stæarð http" +http.getO().size());
        return http;
    }


}
