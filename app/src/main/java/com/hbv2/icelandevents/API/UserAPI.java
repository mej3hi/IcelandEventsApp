package com.hbv2.icelandevents.API;


import com.hbv2.icelandevents.Entities.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Martin on 6.2.2017.
 */

public interface UserAPI {

    @GET("/m/signIn")
    Call<Void> login();
}
