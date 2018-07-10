package com.example.beikeapp.TeacherMain.HomeworkComplete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.AssignActivity;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeworkOutlineActivity extends AppCompatActivity {

    private static final String TAG = "HomeworkOutlineActivity";

    private ListView lvHomeworkOutline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_check_homework_outline);

        initView();

        prepareHomeworkStatus();


    }

    /**
     *  获取作业完成情况
     */
    private void prepareHomeworkStatus() {
        String url = prepareUrl();
        Log.d(TAG, "prepareHomeworkStatus: " + url);
        getHomeworkStatus(url);
    }

    /**
     * 组装url
     */
    private String prepareUrl() {

//        return TeacherConstant.URL_GET_HOMEWORK_STATUS
//                +"?hwId=" + StringUtils.join(Homework.homeworkIdList,",");

        return TeacherConstant.URL_GET_HOMEWORK_STATUS
                +"?account=" + EMClient.getInstance().getCurrentUser();

    }

    private void initView() {
        lvHomeworkOutline = findViewById(R.id.lv_homework_outline_list);
        lvHomeworkOutline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(HomeworkOutlineActivity.this,
                        HomeworkStatusDetailActivity.class).putExtra("position", i + ""));
            }
        });
    }

    /**
     * 请求服务器url
     *
     * @param url 请求url
     */
    private void getHomeworkStatus(String url) {
        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                // 返回值的格式: flag#hwId$title$completion$errRate#hwId$title$completion$errRate#.....
                // 这里解析成数组处理
                // resArray[0] = flag
                // resArray[1] = "hwId$title$completion$errRate"
                // completion:  10/28
                // errRate:  1:25%,2:46%,3:4.7%,......
                String[] resArray = listData.get(0).split("#");

                Log.d(TAG, "onDataReceivedSuccess: " + listData.get(0));

                switch (resArray[0]) {
                    case GlobalConstant.FLAG_SUCCESS:
                        // process data
                        if (resArray[1] != null && !"".equals(resArray[1])) {

                            for (int i = 1; i <= resArray.length - 1; i++) {
                                // 分隔符"$" 不能直接使用，需要"\\$"表示
                                String[] subResArray = resArray[i].split("\\$");
                                String hwId = subResArray[0]; // 好像没什么用
                                String title = subResArray[1];
                                String completion = subResArray[2];
                                String errRate = subResArray[3];
                                HomeworkStatus hs = new HomeworkStatus(title, completion, errRate);
                                HomeworkStatus.homeworkStatusList.add(hs);
                            }
                        }

                        // setup ui
                        List<String> titleList = new ArrayList<>();
                        for (HomeworkStatus hs : HomeworkStatus.homeworkStatusList) {
                            titleList.add(hs.getTitle());
                        }
                        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(HomeworkOutlineActivity.this,
                                android.R.layout.simple_list_item_1, titleList);
                        lvHomeworkOutline.setAdapter(mAdapter);


                        break;
                    case GlobalConstant.FLAG_FAILURE:
                        Log.d(TAG, "获取作业完成情况失败");
                        Toast.makeText(HomeworkOutlineActivity.this, "获取作业完成情况失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d(TAG, "获取作业完成情况失败，检查网络链接！");
                        Toast.makeText(HomeworkOutlineActivity.this, "网络错误,请检查网络链接", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onDataReceivedFailed() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomeworkStatus.homeworkStatusList.clear();
    }
}
