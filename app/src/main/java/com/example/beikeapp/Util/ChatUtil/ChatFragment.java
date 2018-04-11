package com.example.beikeapp.Util.ChatUtil;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * Created by m1821 on 2018/4/10.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {


    @Override
    protected void setUpView() {
        super.setUpView();
        setChatFragmentHelper(this);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    /**
     * 点击右上角图标，进入群组详细信息
     */
    @Override
    public void onEnterToChatDetails() {
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (group == null) {
            Toast.makeText(getActivity(), " no group is found!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(getActivity(),
                GroupDetailsActivity.class).putExtra("groupId", toChatUsername));
    }

    @Override
    public void onAvatarClick(String username) {

    }


    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
