package com.hbv2.icelandevents.HttpRequest;

import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.HttpResponse.HttpResponseEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HI on 14.3.2017.
 */

public class HttpRequestCall {

    /**
     * Asynchronous call executed receiving the backend response
     * when succeeded and posting it to the EventBus. If not succeeded
     * it posts HttpResponseMsg with code 500 to the EventBus
     */
    public static void callReponseMsg(Call<String> call){
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
     * Asynchronous call executed receiving the backend response
     * when succeeded and posting it to the EventBus. If not succeeded
     * it posts HttpResponseMsg with code 500 to the EventBus
     */
    public static void callResponseEvent(Call<List<Event>> call){
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
                List<Event> empty = new ArrayList<Event>();
                EventBus.getDefault().post(new HttpResponseEvent(empty,500));
            }
        });
    }
}
