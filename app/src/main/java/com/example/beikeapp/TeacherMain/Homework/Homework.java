package com.example.beikeapp.TeacherMain.Homework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m1821 on 2018/6/4.
 */

public class Homework {
    /**
     * homework类的数组，用于存放全体作业项
     */
    public static List<Homework> homeworkList = new ArrayList<>();

    /**
     * 作业题目
     */
    private String subject;
    /**
     * 选项A-D
     */
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    /**
     * 答案的位置
     * 0---A
     * 1---B
     * 2---C
     * 3---D
     */
    private int keyPosition;
    // key 中的0-3 对应key[]中的A-D
    private String[] keyArr = {"A", "B", "C", "D"};
    private String key;

    /**
     * 构造方法
     */
    public Homework(String subject, String optionA, String optionB, String optionC, String optionD, int keyPosition) {
        this.subject = subject;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.keyPosition = keyPosition;
    }

    public Homework(String subject, String optionA, String optionB, String optionC, String optionD, String key) {
        this.subject = subject;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.key = key;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setKey(int keyPosition) {
        this.keyPosition = keyPosition;
    }


    public String getSubject() {
        return subject;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public int getKeyPosition() {
        return keyPosition;
    }

    public String getKey() {
        return keyArr[keyPosition];
    }

    public String getStringKey(){return key;}

    public String getHomeworkItemForUrl() {
        return "subject=" + subject
                + "&optionA=" + optionA
                + "&optionB=" + optionB
                + "&optionC=" + optionC
                + "&optionD=" + optionD
                + "&key=" + getKey();


    }
}
