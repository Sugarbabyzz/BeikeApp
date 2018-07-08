package com.example.beikeapp.StudentMain.Homework;

import com.example.beikeapp.TeacherMain.Homework.Homework;

import java.util.ArrayList;
import java.util.List;

public class StudentHomework {

    public static List<StudentHomework> studentHomeworkList = new ArrayList<>();

    //处理作业   title name time size subject optionA optionB optionC optionD key
    private String title;
    private String name;
    private String time;
    private String size;
    private String hwId;


    public StudentHomework(String title, String name, String time, String size, String hwId) {

        this.title = title;
        this.name = name;
        this.time = time;
        this.size = size;
        this.hwId = hwId;

    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setHwId(String hwId) {
        this.hwId = hwId;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getSize() {
        return size;
    }

    public String getHwId() {
        return hwId;
    }
}
