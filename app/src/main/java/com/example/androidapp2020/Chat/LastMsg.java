package com.example.androidapp2020.Chat;

import java.util.List;

public class LastMsg {
    private String text;
    private List<String> members;
    private long time;

    LastMsg() {}

    LastMsg(String text, long time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
