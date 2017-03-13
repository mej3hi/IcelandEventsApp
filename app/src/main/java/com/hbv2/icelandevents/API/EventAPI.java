package com.hbv2.icelandevents.API;

import com.hbv2.icelandevents.Entities.Event;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Martin on 29.1.2017.
 */

public interface EventAPI {

    @GET("/mej3hi/tonlistv2/master/tonlist.json")
    Call<List<Event>> getEvent();

    @GET("/m/")
    Call<List<Event>> getIndex();

    @GET("/m/calander")
    Call<List<Event>> getCalander(@Query("day") String day);

    @Multipart
    @POST("/m/createEvent")
    Call<String> postCreateEvent(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> map);

    @GET("/m/myevents")
    Call<List<Event>> getMyEvents();

    @GET("/m/removeEvent")
    Call<String> getRemoveEvent(@Query("id") Long id);

    @Multipart
    @POST("/m/editEvent")
    Call<String> postEditEvent(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> map);

}
