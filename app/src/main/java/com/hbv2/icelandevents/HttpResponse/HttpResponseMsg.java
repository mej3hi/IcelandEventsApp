package com.hbv2.icelandevents.HttpResponse;

/**
 * Created by Martin on 8.3.2017.
 */

public class HttpResponseMsg {

    private String msg;
    private int code;

    public HttpResponseMsg(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
