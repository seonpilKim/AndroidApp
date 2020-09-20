package com.example.androidapp2020.Board.ListVO;

public class CommVO {
    private String id;
    private String content;
    private String time;


    public CommVO(){}
    public CommVO(String content) {this.content = content;}

    public String getID() {return id;}

    public void setID(String id) {this.id = id;}

    public String getContent() { return content; }

    public void setContent(String content) {this.content = content;}

    public String getTime() {return time;}

    public void setTime(String time) {this.time = time;}

}
