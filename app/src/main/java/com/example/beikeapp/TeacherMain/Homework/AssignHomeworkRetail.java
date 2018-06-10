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

public class AssignHomeworkRetail extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AssignHomeworkRetail";

    private int position;

    private Homework hw;
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
        setContentView(R.layout.teacher_main_assign_homework_retail);
        position = getIntent().getIntExtra("position",0);
        initView();
        setupUI();
    }

    private void initView() {
        etSubject = findViewById(R.id.et_subject);
        etOptionA = findViewById(R.id.et_option_a);
        etOptionB = findViewById(R.id.et_option_b);
        etOptionC = findViewById(R.id.et_option_c);
        etOptionD = findViewById(R.id.et_option_d);

        spinner = findViewById(R.id.spinner);
        btnComplete = findViewById(R.id.btn_complete);

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
        if (view.getId() == R.id.btn_complete) {
                if (testValidity()){
                    setHomeworkList(); //修改对应HomeworkList中记录
                    startActivity(new Intent(this,AssignResult.class));
                } else {
                    Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                }

        }
    }

    /**
     * 修改对应题目
     */
    private void setHomeworkList() {
        hw.setSubject(subject);
        hw.setOptionA(optionA);
        hw.setOptionB(optionB);
        hw.setOptionC(optionC);
        hw.setOptionD(optionD);
        hw.setKey(key);
    }

    /**
     * 点击返回上一题后setup一下UI
     */
    private void setupUI() {
        hw = Homework.homeworkList.get(position);
        etSubject.setText(hw.getSubject());
        etOptionA.setText(hw.getOptionA());
        etOptionB.setText(hw.getOptionB());
        etOptionC.setText(hw.getOptionC());
        etOptionD.setText(hw.getOptionD());
        spinner.setSelection(hw.getKeyPosition());
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
