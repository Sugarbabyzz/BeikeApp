package com.example.beikeapp.StartPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentRegister.StudentLogin;

/**
 * Created by SugarSugar on 2018/3/31.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //启动一个延迟线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    /*
                        延迟3秒
                     */
                    Thread.sleep(3000);
                    SharedPreferences preferences = getSharedPreferences("count", 0);
                    int count = preferences.getInt("count" , 0 );

                    /**
                     *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
                     */
                    if (count == 0){
                        startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    }else{
                        startActivity(new Intent(WelcomeActivity.this, StudentLogin.class));
                    }
                    finish();

                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putInt("count", 1); // 存入数据
                    //提交修改
                    editor.commit();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
