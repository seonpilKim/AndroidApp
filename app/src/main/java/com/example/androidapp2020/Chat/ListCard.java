package com.example.androidapp2020.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ListCard {
    private String userName;
    private List<String> members;
    private String recentMsg;
    private long time;
    private boolean groupChat;

    public ListCard (Object userName, String recentMsg, long time, boolean groupChat) {
        this.recentMsg = recentMsg;
        this.time = time;
        this.groupChat = groupChat;

        if(groupChat)
            this.members = (List<String>) userName;
        else
            this.userName = (String) userName;
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

    public boolean isGroupChat() {
        return groupChat;
    }

    public void setGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
