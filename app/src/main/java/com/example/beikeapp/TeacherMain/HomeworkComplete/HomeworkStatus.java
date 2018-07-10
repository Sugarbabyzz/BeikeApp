package com.example.beikeapp.TeacherMain.HomeworkComplete;

import java.util.ArrayList;
import java.util.List;

public class HomeworkStatus {

    // 所有作业完成情况的列表
    public static List<HomeworkStatus> homeworkStatusList = new ArrayList<>();

    //作业标题
    private String title;

    // 学生完成情况
    private String completion;

    // 错题情况
    private String errRate;

    public HomeworkStatus(String title, String completion, String errRate) {
        this.title = title;
        this.completion = completion;
        this.errRate = errRate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getErrRate() {
        return errRate;
    }

    public void setErrRate(String errRate) {
        this.errRate = errRate;
    }
}
