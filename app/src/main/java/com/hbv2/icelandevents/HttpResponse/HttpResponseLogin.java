package com.hbv2.icelandevents.HttpResponse;

/**
 * Created by Martin on 21.2.2017.
 */

public class HttpResponseLogin {

    private int code;

    public HttpResponseLogin(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
