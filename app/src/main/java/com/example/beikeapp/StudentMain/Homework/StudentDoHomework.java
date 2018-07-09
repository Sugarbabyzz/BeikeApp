package com.example.beikeapp.StudentMain.Homework;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.InitApp.MyApplication;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.Activity.CardActivity;
import com.example.beikeapp.StudentMain.Activity.QuestionActivity;
import com.example.beikeapp.StudentMain.Activity.ResultActivity;
import com.example.beikeapp.StudentMain.DBHelper.MyTag;
import com.example.beikeapp.StudentMain.DBHelper.ToolHelper;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.xiaomi.mipush.sdk.HWPushHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentDoHomework extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StudentDoHomework";

    private int i;
    private String temp;
    private String hwId, size, title;
    private ImageView imgCollect, imgCard;
    private Chronometer chronometer;
    private ImageView imgPre, imgNext;
    private AdapterViewFlipper vf;
    private BaseAdapter adapter;
    private ProgressBar pb;
    private View root;
    public static List<String> anList;
    private int score = 0, index = 0;
    private TextView tvTitle, tvScore;
    private TextView tvQue, tvDetail, tvAns, tvYou;
    private CheckBox cb1, cb2, cb3, cb4;
    private boolean isFirst = false;
    private String que, A, B, C, D, answer;

    private StringBuffer errList = new StringBuffer();   //保存错题序号



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar_que);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hwId = getIntent().getStringExtra("hwId");
        size = getIntent().getStringExtra("size");
        title = getIntent().getStringExtra("title");
        temp = getIntent().getStringExtra("i");
        i = Integer.valueOf(temp);
        System.out.println("查看第i条作业：" + i + "\t\t当前hwId：" + hwId + "\t\t作业数目：" + size);


        //初始化界面
        initView();


    }

    private void initView() {

        //初始化收藏按钮
        imgCollect = findViewById(R.id.img_collect);
        imgCollect.setOnClickListener(this);
        imgCollect.setVisibility(View.GONE);
        //初始化答题卡按钮
        imgCard = findViewById(R.id.img_card);
        imgCard.setOnClickListener(this);
        //初始化计时器
        chronometer = findViewById(R.id.mytime);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - chronometer.getBase() == 3600) {
                    Toast.makeText(StudentDoHomework.this, "已过1小时。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置标题
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
        //学生给出的答案List初始化
        anList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(size); i++) {
            anList.add("");
        }
        //设置进度条
        pb = findViewById(R.id.pb);
        pb.setMax(Integer.parseInt(size)-1);
        pb.setProgress(0);
        //前后按钮
        imgPre = findViewById(R.id.img_pre);
        imgNext = findViewById(R.id.img_next);
        imgPre.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        //设置初始分数
        tvScore = findViewById(R.id.tv_num);
        tvScore.setText("得分：" + String.valueOf(score) + "/" + String.valueOf(size));
        tvScore.setVisibility(View.GONE);
        //设置ViewFlipper
        vf = findViewById(R.id.vf);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return Integer.parseInt(size);
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                index = position;
                createView(position);
                return root;
            }
        };
        vf.setAdapter(adapter);
    }

    //答题卡设置
    private void createView(int pos) {

        root = LayoutInflater.from(StudentDoHomework.this).inflate(R.layout.question_item, null);
        tvQue = root.findViewById(R.id.tv_que1);
        cb1 = root.findViewById(R.id.cb_choice1);
        cb2 = root.findViewById(R.id.cb_choice2);
        cb3 = root.findViewById(R.id.cb_choice3);
        cb4 = root.findViewById(R.id.cb_choice4);
        tvAns = root.findViewById(R.id.tv_answer1);
        tvDetail = root.findViewById(R.id.tv_detail1);
        tvYou = root.findViewById(R.id.tv_you);

        //获取数据
        que = Homework.homeworkList.get(pos).getSubject();
        A = "A." + Homework.homeworkList.get(pos).getOptionA();
        B = "B." + Homework.homeworkList.get(pos).getOptionB();
        C = "C." + Homework.homeworkList.get(pos).getOptionC();
        D = "D." + Homework.homeworkList.get(pos).getOptionD();
        answer = Homework.homeworkList.get(pos).getStringKey();
        //加载内容
        tvQue.setText((pos + 1) + ".( 单选 )" + que);
        cb1.setText(A);
        cb2.setText(B);
        cb3.setText(C);
        cb4.setText(D);
        cb1.setButtonDrawable(R.drawable.cb);
        cb2.setButtonDrawable(R.drawable.cb);
        cb3.setButtonDrawable(R.drawable.cb);
        cb4.setButtonDrawable(R.drawable.cb);
        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb3.setEnabled(true);
        cb4.setEnabled(true);
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        tvAns.setText("【正确答案】" + answer);
        if (anList.get(pos).equals("")) {
            tvAns.setVisibility(View.GONE);
            tvYou.setVisibility(View.GONE);
            tvDetail.setVisibility(View.GONE);
        } else {
            //已答题设置为不可操作
            disableChecked(pos);
        }
        //设置当前进度
        pb.setProgress(pos);
        //滑动切换
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startX = v.getWidth() / 2, endX = v.getWidth() / 2, min = 100;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        break;
                }
                if (startX - endX > min) {
                    vf.showNext();
                } else if (endX - startX > min) {
                    vf.showPrevious();
                }
                return true;
            }
        });

    }

    //判断选择答案对错
    private void isAnswerTrue(int pos) {
        if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked()) {
            //获取答案
            StringBuffer sb = new StringBuffer();
            if (cb1.isChecked()) sb.append("A");
            if (cb2.isChecked()) sb.append("B");
            if (cb3.isChecked()) sb.append("C");
            if (cb4.isChecked()) sb.append("D");
            String you = sb.toString();
            //保存答案
            anList.set(pos, you);
            //判断对错
            if (you.equals(answer)) {
                moveCorrect();
            } else {

                //将错题加入errList
                if (tvAns.getVisibility() == View.GONE){
                    Log.d(TAG, "isAnswerTrue: 当前pos为:" + (pos+1));
                    errList.append((pos+1) + ",");
                    Log.d(TAG, "isAnswerTrue: 错题序号为：" + errList);
                }

                //错误则保存错题，显示答案
                disableChecked(pos);

            }
        } else {
            Toast.makeText(StudentDoHomework.this, "请选择答案", Toast.LENGTH_SHORT).show();
        }
    }

    //移除正确题目
    @SuppressLint("SetTextI18n")
    private void moveCorrect() {
        score++;
        tvScore.setText("得分：" + String.valueOf(score) + "/" + String.valueOf(size));
        vf.showNext();

    }

    //已做题不可再做
    private void disableChecked(int pos) {
        tvYou.setText("【你的答案】" + anList.get(pos));
        tvAns.setVisibility(View.VISIBLE);
        tvYou.setVisibility(View.VISIBLE);
        if (answer.contains("A")) cb1.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("B")) cb2.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("C")) cb3.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("D")) cb4.setButtonDrawable(R.drawable.cb_right);
        //设置为不可答题
        cb1.setEnabled(false);
        cb2.setEnabled(false);
        cb3.setEnabled(false);
        cb4.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.que, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.que_ok://提交答案
                if (index >= Integer.parseInt(size) - 1) {
                    if (!isFirst) {
                        isAnswerTrue(index);
                        isFirst = true;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("是否提交作业？");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveExam();
                            }
                        });
                        builder.show();
                    }
                } else {
                    isAnswerTrue(index);
                }
                break;
            case android.R.id.home://返回
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否取消正在做的作业？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StudentDoHomework.this.finish();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //保存考试记录，并提交结果
    private void saveExam() {

        //将作业结果提交到服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                addHomeworkResult();
            }
        }).start();


        chronometer.stop();
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String mytime = chronometer.getText().toString();
        String mydate = ft.format(date);
        String title = "本次作业";
        Intent intent = new Intent(this, StudentHomeworkResult.class);
        intent.putExtra("score", score + "/" + size);
        intent.putExtra("time", mytime);
        intent.putExtra("date", mydate);
        intent.putExtra("title", title);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_next:
                vf.showNext();
                break;
            case R.id.img_pre:
                vf.showPrevious();
                break;
            case R.id.img_card:
                Intent intent = new Intent(this, CardActivity.class);
                intent.putExtra("num", Integer.parseInt(size));
                intent.putExtra("from", 3);
                startActivityForResult(intent, MyTag.CARD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyTag.CARD && resultCode == MyTag.CARD) {
            int select = data.getIntExtra("card", 0);
            moveToItem(select);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void moveToItem(int t) {
        if (t != index) {
            if (t > index) {
                int d = t - index;
                for (int i = 0; i < d ; i++)
                    vf.showNext();
            } else if (t < index) {
                int p = index - t;
                for (int i = 0; i < p ; i++)
                    vf.showPrevious();
            }
        }
    }

    /**
     *  提交作业结果到服务器
     */
    private void addHomeworkResult() {
        //URL待修改
        String addHomeworkResult = StudentConstant.addHomeworkResultURL
                + "?hwId=" + hwId
                + "&err=" + errList.toString();

        MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
        a.execute(addHomeworkResult);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if(listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    //提交作业结果成功
                    Log.d(TAG, "onDataReceivedSuccess: 提交作业结果成功！");
                }else {
                    //提交作业结果失败
                    Log.d(TAG, "onDataReceivedSuccess: 提交作业结果失败！");
                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }
}
