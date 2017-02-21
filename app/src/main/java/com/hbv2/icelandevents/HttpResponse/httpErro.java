package com.hbv2.icelandevents.HttpResponse;

import java.util.List;

/**
 * Created by Martin on 21.2.2017.
 */

public class httpErro {

    private List o;
    private String erro;
    private String failure;
    private int code;

    public httpErro(List o, String erro, String failure, int code) {
        this.o = o;
        this.erro = erro;
        this.failure = failure;
        this.code = code;
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
