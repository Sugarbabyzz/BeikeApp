package com.example.beikeapp.Util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.beikeapp.StudentNotify.Notify.NotifyActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PushMesssageReceiver广播接收器

 PushMessageReceiver是一个抽象的BroadcastReceiver类，
  它的用途有两种:
    1、获取服务器推送的消息
    2、获取调用MiPushClient方法的返回结果
 */
public class MessageReceiver extends PushMessageReceiver {
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    private Pattern pattern;
    private Matcher matcher;

    /**
     * onReceivePassThroughMessage用来接收服务器发送的透传消息，
     * @param context
     * @param message
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }

        //打印消息方便测试
        System.out.println("透传消息到达了");
        System.out.println("透传消息是"+message.toString());
    }

    /**
     * onNotificationMessageClicked用来接收服务器发来的通知栏消息（用户点击通知栏时触发）
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }

        pattern = Pattern.compile("(category=\\{)(.*?)(})");
        matcher = pattern.matcher(message.toString());
        String category;
        if (matcher.matches()){
            category = matcher.group(2);
            if (category.equals("notify")){
                //start
                Intent intent = new Intent(context, NotifyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            } else if (category.equals("homework")){
                //start
            }
        }






        //打印消息方便测试
        System.out.println("用户点击了通知消息");
        System.out.println("通知消息是" + message.toString());
        System.out.println("点击后,会进入应用" );

    }

    /**
     * onNotificationMessageArrived用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }



        pattern = Pattern.compile("(category=\\{)(.*?)(})");
        matcher = pattern.matcher(message.toString());
        String category;
        if (matcher.matches()){
            category = matcher.group(2);
            if (category.equals("notify")){
                /*
                 * 接收通知 title
                 */
                pattern = Pattern.compile("(title=\\{)(.*?)(})");
                matcher = pattern.matcher(message.toString());
                String title;
                if (matcher.matches()){
                    title = matcher.group(2);
                }

                /*
                 * 接收通知 content
                 */
                pattern = pattern.compile("(content=\\{)(.*?)(})");
                matcher = pattern.matcher(message.toString());
                String content;
                if (matcher.matches()){
                    content = matcher.group(2);
                }


            } else if (category.equals("homework")){
                //start 作业 待写
            }
        }





        //打印消息方便测试
        System.out.println("通知消息到达了");
        System.out.println("通知消息是"+message.toString());

    }

    /**
     * onCommandResult用来接收客户端向服务器发送命令消息后返回的响应
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }

        //test
        System.out.println(command );


        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印信息便于测试注册成功与否
                System.out.println("注册成功");

            } else {
                System.out.println("注册失败");
            }
        }
    }

    /**
     * onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应
     * @param context
     * @param message
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }

        //test
        System.out.println(command );

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印日志：注册成功
                System.out.println("注册成功");
            } else {
                //打印日志：注册失败
                System.out.println("注册失败");
            }
        } else {
            System.out.println("其他情况"+message.getReason());
        }

    }

}