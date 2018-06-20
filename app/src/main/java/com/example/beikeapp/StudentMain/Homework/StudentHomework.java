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
    private List<Homework> homeworkList;

    public StudentHomework(String title, String name, String time, String size, List<Homework> homeworkList){

        this.title = title;
        this.name = name;
        this.time = time;
        this.size = size;
        this.homeworkList = homeworkList;
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

    public void setHomeworkList(List<Homework> homeworkList) {
        this.homeworkList = homeworkList;
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

    public List<Homework> getHomeworkList() {
        return homeworkList;
    }
}
