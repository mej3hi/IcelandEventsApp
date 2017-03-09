package com.hbv2.icelandevents.API;

import com.hbv2.icelandevents.Entities.Event;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Martin on 29.1.2017.
 */

public interface EventAPI {

    @GET("/mej3hi/tonlistv2/master/tonlist.json")
    Call<List<Event>> getEvent();

    @GET("/m/")
    Call<List<Event>> getIndex();

    @GET("/m/calander")
    Call<List<Event>> getCalander(@Path("day") String day);

    @Multipart
    @POST("/m/createEvent")
    Call<Void> postCreateEvent(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    //@GET("/m/myevents")
    @GET("/mej3hi/tonlistv2/master/tonlist.json")
    Call<List<Event>> getMyEvents();

    @GET("/m/removeEvent")
    Call<Void> getRemoveEvent(@Path("id") Long id);

    @GET("/m/editEvent")
    Call<Void> getEditEvent(@Body Event event, @Part MultipartBody.Part image, @Part("name") RequestBody name);


}
