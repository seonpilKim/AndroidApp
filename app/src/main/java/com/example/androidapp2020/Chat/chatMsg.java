package com.example.androidapp2020.Chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class chatMsg {
    private String text;
    private String sentBy;
    private long time;
    private boolean isMine;

    public chatMsg(String text, String sentBy, boolean isMine, long time) {
        this.text = text;
        this.sentBy = sentBy;
        this.isMine = isMine;
        this.time = time;
    }

    public chatMsg() {}


    public String getText() {
        return text;
    }

    public String getSentBy() {
        return sentBy;
    }

    public long getTime() {
        return time;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIsMine(String myID) {
        if(this.sentBy == myID)
            this.isMine = true;
        else
            this.isMine = false;
    }
}
