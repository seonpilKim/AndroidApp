package com.example.androidapp2020.Chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ListCard {
    private String userName;
    private String recentMsg;
    private long time;

    public ListCard (String userName, String recentMsg, long time) {
        this.userName = userName;
        this.recentMsg = recentMsg;
        this.time = time;
    }

    public ListCard() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(String recentMsg) {
        this.recentMsg = recentMsg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
