package com.example.androidapp2020.Chat;

import java.util.Date;

public class chatMsg {
    private String text;
    private String user;
    private long time;
    private boolean isMine;

    public chatMsg(String text, String user, boolean isMine) {
        this.text = text;
        this.user = user;
        this.isMine = isMine;
        time = new Date().getTime();
    }

    public chatMsg() {}

    public chatMsg(String toString, String displayName) {
    }

    public String getText() {
        return text;
    }

    public String getUser() {
        return user;
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

    public void setUser(String user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }
}