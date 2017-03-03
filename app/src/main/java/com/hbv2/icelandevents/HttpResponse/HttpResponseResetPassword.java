package com.hbv2.icelandevents.HttpResponse;

/**
 * Created by Satan on 2.3.2017.
 */

public class HttpResponseResetPassword {
    private int code;

    public HttpResponseResetPassword(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

