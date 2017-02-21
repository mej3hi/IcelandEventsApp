package com.hbv2.icelandevents.HttpResponse;

import com.hbv2.icelandevents.Entities.Event;

import java.util.List;

/**
 * Created by Martin on 10.2.2017.
 */

public class Http {




    private List o;
    private String erro;
    private String failure;
    private int code;

    public Http(List o) {
        this.o = o;
    }

    public List getO() {
        return o;
    }

    public void setO(List o) {
        this.o = o;
    }

     public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



}
