package com.example.androidapp2020.Chat;

import java.util.List;

public class LastMsg {
    private String text;
    private List<String> members;
    private long time;
    private boolean groupChat;

    LastMsg() {}

    LastMsg(String text, long time, boolean groupChat) {
        this.text = text;
        this.time = time;
        this.groupChat = groupChat;
    }

    LastMsg(String text, long time, boolean groupChat, List<String> members) {
        this.text = text;
        this.time = time;
        this.groupChat = groupChat;
        this.members = members;
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
