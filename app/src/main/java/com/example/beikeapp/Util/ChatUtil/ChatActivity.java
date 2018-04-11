package com.example.beikeapp.Util.ChatUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.beikeapp.R;
import com.example.beikeapp.TeacherJiaXiao.JiaXiaoFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;


public class ChatActivity extends ChatBaseActivity{

   // private EaseChatFragment chatFragment;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);

        //get user id or group id

        //use EaseChatFratFragment
        EaseChatFragment chatFragment = new EaseChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }


}
