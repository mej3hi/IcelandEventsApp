package com.hbv2.icelandevents.HttpResponse;

import com.hbv2.icelandevents.Entities.Event;

import java.util.List;

/**
 * Created by Martin on 10.2.2017.
 */

public class HttpEvent {

    private List<Event> listEvent;
    private int code;

    public HttpEvent(List<Event> listEvent, int code) {
        this.listEvent = listEvent;
        this.code = code;
    }

    public List<Event> getListEvent() {
        return listEvent;
    }

    public void setListEvent(List<Event> listEvent) {
        this.listEvent = listEvent;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }





}
