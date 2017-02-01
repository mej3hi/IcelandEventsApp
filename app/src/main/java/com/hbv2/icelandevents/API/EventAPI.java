package com.hbv2.icelandevents.API;

import com.hbv2.icelandevents.Entities.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Martin on 29.1.2017.
 */

public interface EventAPI {

    @GET("/m/")
    Call<List<Event>> getEvent();
}
