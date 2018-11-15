package com.ya.simplegames.ajax;

import java.sql.Timestamp;

public class AjaxResponse {

    private String status;
    private String data;
    private String data2;
    private Timestamp timeMark;

    public AjaxResponse(){
        this.status = "";
        this.data = "";
        this.data2 = "";
        this.timeMark = null;
    }

    public AjaxResponse(String status){
        this.status = status;
        this.data = "";
        this.data2 = "";
        this.timeMark = null;
    }

    public AjaxResponse(String status, String data){
        this.status = status;
        this.data = data;
        this.data2 = "";
        this.timeMark = null;
    }

    public AjaxResponse(String status, String data, String data2) {
        this.status = status;
        this.data = data;
        this.data2 = data2;
        this.timeMark = null;
    }

    public AjaxResponse(String status, String data, String data2, Timestamp timeMark) {
        this.status = status;
        this.data = data;
        this.data2 = data2;
        this.timeMark = timeMark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData2() { return data2; }

    public void setData2(String data2) { this.data2 = data2; }

    public Timestamp getTimeMark() { return timeMark; }

    public void setTimeMark(Timestamp timeMark) { this.timeMark = timeMark; }
}
