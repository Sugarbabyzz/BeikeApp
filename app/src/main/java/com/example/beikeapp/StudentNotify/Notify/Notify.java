package com.example.beikeapp.StudentNotify.Notify;

import java.util.ArrayList;
import java.util.List;

public class Notify {

    public static List<Notify> notifyList = new ArrayList<>();

    private String title;
    private String content;
    private String name;
    private String time;

    public Notify(String title, String content, String name, String time){

        this.title = title;
        this.content = content;
        this.name = name;
        this.time = time;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
