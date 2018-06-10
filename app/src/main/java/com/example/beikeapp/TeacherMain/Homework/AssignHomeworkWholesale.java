package com.example.beikeapp.TeacherMain.Homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.beikeapp.R;

public class AssignHomeworkWholesale extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AssignHomeworkWholesale";

    private static int CURSOR = 0;

    private static int LATEST_PAGE = 0;
    /**
     * 作业题目
     */
    private EditText etSubject;
    /**
     * 四个选项
     */
    private EditText etOptionA;
    private EditText etOptionB;
    private EditText etOptionC;
    private EditText etOptionD;
    /**
     * 参考答案
     */
    private Spinner spinner;
    /**
     * 返回编辑上一题按钮
     */
    private Button btnBackToPrevious;
    /**
     * 继续编辑下一题按钮
     */
    private Button btnNext;
    /**
     * 完成编辑按钮
     */
    private Button btnComplete;

    /**
     * 作业内容字符串
     */
    private String subject;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_assign_homework_wholesale);
        initView();
    }

    private void initView() {
        etSubject = findViewById(R.id.et_subject);
        etOptionA = findViewById(R.id.et_option_a);
        etOptionB = findViewById(R.id.et_option_b);
        etOptionC = findViewById(R.id.et_option_c);
        etOptionD = findViewById(R.id.et_option_d);

        spinner = findViewById(R.id.spinner);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        btnNext = findViewById(R.id.btn_next);
        btnComplete = findViewById(R.id.btn_complete);

        btnBackToPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                key = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_to_previous:
                if (CURSOR == 0) {
                    Toast.makeText(this, "已是第一页!", Toast.LENGTH_SHORT).show();
                } else {
                    if (CURSOR == LATEST_PAGE) {
                        subject = etSubject.getText().toString().trim();
                        optionA = etOptionA.getText().toString().trim();
                        optionB = etOptionB.getText().toString().trim();
                        optionC = etOptionC.getText().toString().trim();
                        optionD = etOptionD.getText().toString().trim();
                        key = spinner.getSelectedItemPosition();

                        saveCurrentHomework();
                    }
                    CURSOR--;
                    setupUI();
                }
                break;
            case R.id.btn_next:
                if (LATEST_PAGE == 0) {
                    createNewPage();
                } else {
                    if (CURSOR < LATEST_PAGE) {
                        CURSOR++;
                        setupUI();
                    } else {
                        createNewPage();
                    }
                }
                break;
            case R.id.btn_complete:
                if (CURSOR < LATEST_PAGE) {
                    startActivity(new Intent(this, AssignResult.class));
                } else {
                    if (testValidity()) {
                        saveCurrentHomework();
                        startActivity(new Intent(this, AssignResult.class));
                        finish();
                    } else {
                        Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    /**
     * 点击返回上一题时，保存一下当前正在编辑的页面状态
     */
    private void saveCurrentHomework() {
        Homework hw = new Homework(subject, optionA, optionB, optionC, optionD, key);
        Homework.homeworkList.add(hw);
    }

    /**
     * 点击布置下一题,创建新的页面
     */
    private void createNewPage() {

        key = spinner.getSelectedItemPosition();

        if (testValidity()) {
            Homework hw = new Homework(subject, optionA, optionB, optionC, optionD, key);
            Homework.homeworkList.add(hw);
            CURSOR++;
            LATEST_PAGE++;
            resetUI();
        } else {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击返回上一题后setup一下UI
     */
    private void setupUI() {
        Homework hw = Homework.homeworkList.get(CURSOR);
        etSubject.setText(hw.getSubject());
        etOptionA.setText(hw.getOptionA());
        etOptionB.setText(hw.getOptionB());
        etOptionC.setText(hw.getOptionC());
        etOptionD.setText(hw.getOptionD());
        spinner.setSelection(hw.getKeyPosition());
    }

    /**
     * 重置界面
     */
    private void resetUI() {
        etSubject.setText("");
        etOptionA.setText("");
        etOptionB.setText("");
        etOptionC.setText("");
        etOptionD.setText("");
        spinner.setSelection(0);
    }

    /**
     * 检查输入合法性
     *
     * @return
     */
    private boolean testValidity() {

        subject = etSubject.getText().toString().trim();
        optionA = etOptionA.getText().toString().trim();
        optionB = etOptionB.getText().toString().trim();
        optionC = etOptionC.getText().toString().trim();
        optionD = etOptionD.getText().toString().trim();

        return !(subject.equals("") || optionA.equals("")
                || optionB.equals("") || optionC.equals("")
                || optionD.equals(""));
    }
}
